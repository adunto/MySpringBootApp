package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        return ResponseEntity.ok(bookRepository.save(newBook));
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        ResponseEntity<Book> book = bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        return book;
    }

    @GetMapping("isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found.", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(book);
    }

    @GetMapping("author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        return bookRepository.findById(id)
                .map(existBook -> {
                    existBook.setTitle(bookDetail.getTitle());
                    existBook.setAuthor(bookDetail.getAuthor());
                    existBook.setIsbn(bookDetail.getIsbn());
                    existBook.setPrice(bookDetail.getPrice());
                    existBook.setPublishDate(bookDetail.getPublishDate());

                    Book updatedBook = bookRepository.save(existBook);
                    return ResponseEntity.ok(updatedBook);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(existBook -> {
                    bookRepository.delete(existBook);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
