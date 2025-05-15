package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.BookDTO;
import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.BookDetail;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.exception.ErrorCode;
import com.basic.myspringboot.repository.BookDetailRepository;
import com.basic.myspringboot.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

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
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "author", author);
        }
        return list;
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        List<BookDTO.Response> list = bookRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(BookDTO.Response::fromEntity).toList();
        if (list.isEmpty()) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "title", title);
        }
        return list;
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        // isbn 중복 검사
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

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
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
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
                                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id))
        );
    }

}
