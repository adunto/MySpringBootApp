package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.BookDTO;
import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.exception.BusinessException;
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

    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.BookResponse::from).toList();
    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    public List<BookDTO.BookResponse> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream().map(BookDTO.BookResponse::from).toList();
    }

    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        // isbn 중복 검사
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(
                        user -> {
                            throw new BusinessException("해당 도서의 isbn이 이미 존재합니다. : " + request.getIsbn(),
                                    HttpStatus.CONFLICT);
                        }
                );

        Book book = request.toEntity();
        Book savedBook = bookRepository.save(book);
        return BookDTO.BookResponse.from(savedBook);
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id,
                                           BookDTO.BookUpdateRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        Book updatedBook = bookRepository.save(book);
        return BookDTO.BookResponse.from(updatedBook);
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
