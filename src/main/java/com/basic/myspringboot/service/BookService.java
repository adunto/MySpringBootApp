package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.BookDTO;
import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.BookDetail;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.BookDetailRepository;
import com.basic.myspringboot.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity).toList();
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream().map(BookDTO.Response::fromEntity).toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(BookDTO.Response::fromEntity).toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        // isbn 중복 검사
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(
                        user -> {
                            throw new BusinessException("해당 도서의 isbn이 이미 존재합니다. : " + request.getIsbn(),
                                    HttpStatus.CONFLICT);
                        }
                );

        Book book = Book.builder().title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate()).build();

        BookDTO.BookDetailDTO d = request.getDetailRequest();


        BookDetail bookDetail = BookDetail.builder()
                .description(d.getDescription())
                .language(d.getLanguage())
                .pageCount(d.getPageCount())
                .publisher(d.getPublisher())
                .coverImageUrl(d.getCoverImageUrl())
                .edition(d.getEdition()).build();

        book.setBookDetail(bookDetail);
        bookDetail.setBook(book);

        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id,
                                           BookDTO.Request request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.delete(
                bookRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException("Book Not Found", HttpStatus.NOT_FOUND))
        );
    }

}
