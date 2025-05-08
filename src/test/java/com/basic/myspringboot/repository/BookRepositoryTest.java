package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.Customer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(BookRepositoryTest.class);

    @Autowired
    BookRepository bookRepository;

    @Test
    @Rollback(value = false)
    void testCreateBook() {
        // 도서 등록 테스트
        Book book1 = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.parse("2025-05-07"))
                .build();

        Book book2 = Book.builder()
                .title("JPA 프로그래밍")
                .author("박둘리")
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.parse("2025-04-30"))
                .build();

        // book1 추가
        Book addBook = bookRepository.save(book1);
        assertThat(addBook).isNotNull();
        log.debug("추가됨 : " + addBook.getTitle());

        // book2 추가
        addBook = bookRepository.save(book2);
        assertThat(addBook).isNotNull();
        log.debug("추가됨 : " + addBook.getTitle());
    }

    @Test
    void testFindByIsbn() {
        Book book = bookRepository.findByIsbn("9788956746425")
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        assertThat(book.getIsbn()).isEqualTo("9788956746425");
    }

    @Test
    void testFindByAuthor() {
        Book book = bookRepository.findByAuthor("홍길동")
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        assertThat(book.getAuthor()).isEqualTo("홍길동");
    }

    @Test
    @Rollback(value = false)
    void testUpdateBook() {
        Book book = bookRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        book.setTitle(book.getTitle()+" : 수정됨");
        bookRepository.save(book);
    }

    @Test
    @Rollback(value = false)
    void testDeleteBook() {
        Book book = bookRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        bookRepository.delete(book);
    }
}