package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.BookDTO;
import com.basic.myspringboot.controller.dto.PublisherDTO;
import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.BookDetail;
import com.basic.myspringboot.entity.Publisher;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.exception.ErrorCode;
import com.basic.myspringboot.repository.BookDetailRepository;
import com.basic.myspringboot.repository.BookRepository;
import com.basic.myspringboot.repository.PublisherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    private final PublisherRepository publisherRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity).toList();
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "isbn", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        List<BookDTO.Response> list = bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream().map(BookDTO.Response::fromEntity).toList();
        if (list.isEmpty()) {
//            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "author", author);
            return new ArrayList<>();
        }
        return list;
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        List<BookDTO.Response> list = bookRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(BookDTO.Response::fromEntity).toList();
        if (list.isEmpty()) {
//            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "title", title);
            return new ArrayList<>();
        }
        return list;
    }

    public List<BookDTO.Response> getBooksByPublisherId(Long publisherId) {
        List<BookDTO.Response> list = bookRepository.findByPublisherId(publisherId)
                .stream().map(BookDTO.Response::fromEntity).toList();
        if (list.isEmpty()) {
//            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "publisherId", publisherId);
            return new ArrayList<>();
        }
        return list;
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        // isbn 중복 검사
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();


        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO detailDto = request.getDetailRequest();
            BookDetail bookDetail = BookDetail.builder()
                    .description(detailDto.getDescription())
                    .language(detailDto.getLanguage())
                    .pageCount(detailDto.getPageCount())
                    .coverImageUrl(detailDto.getCoverImageUrl())
                    .edition(detailDto.getEdition())
                    .build();

            // 양방향 관계 설정
            book.setBookDetail(bookDetail);
            bookDetail.setBook(book);
        }

        if (publisherRepository.findById(request.getPublisherId()).isPresent()) {
            Publisher publisher = publisherRepository.findById(request.getPublisherId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", request.getPublisherId()));

            book.setPublisher(publisher); // Book에 Publisher 설정
            publisher.addBook(book);
        }

        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id,
                                         BookDTO.Request request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        BookDTO.BookDetailDTO detailDto = request.getDetailRequest();
        BookDetail bookDetail = book.getBookDetail();
        if (bookDetail == null) {
            bookDetail = BookDetail.builder().book(book).build();
            book.setBookDetail(bookDetail);
        }

        bookDetail.setDescription(detailDto.getDescription());
        bookDetail.setLanguage(detailDto.getLanguage());
        bookDetail.setPageCount(detailDto.getPageCount());
        bookDetail.setCoverImageUrl(detailDto.getCoverImageUrl());
        bookDetail.setEdition(detailDto.getEdition());

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", request.getPublisherId()));

        Publisher currentPublisher = book.getPublisher();

        if (publisherRepository.findById(request.getPublisherId()).isPresent()) {

            Publisher publisherToSet = publisherRepository.findByName(publisher.getName())
                    .orElseGet(() -> {
                        Publisher newPublisher = Publisher.builder()
                                .name(publisher.getName())
                                .establishedDate(publisher.getEstablishedDate())
                                .address(publisher.getAddress())
                                .build();
                        return publisherRepository.save(newPublisher);
                    });

            // Book과 새 Publisher 관계 설정
            if (currentPublisher != publisherToSet) { // Publisher가 변경되었거나 새로 설정된 경우
                if (currentPublisher != null) {
                    currentPublisher.removeBook(book); // 이전 Publisher에서 Book 제거
                }
                book.setPublisher(publisherToSet);
                publisherToSet.addBook(book); // 새 Publisher에 Book 추가 (양방향)
            }
        } else {
            if (currentPublisher != null) {
                currentPublisher.removeBook(book); // Publisher의 books 컬렉션에서 제거
                book.setPublisher(null);
            }
        }

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));

        Publisher publisher = book.getPublisher();
        if (publisher != null) {
            publisher.removeBook(book);
        }

        bookRepository.delete(book);
    }

}
