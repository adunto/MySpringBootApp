package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    @Query("SELECT b from Book b JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    @Query("SELECT b from Book b JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByPublisherId(Long publisherId);

    Long countByPublisherId(Long publisherId);

    @Query("SELECT b from Book b LEFT JOIN FETCH b.bookDetail bd LEFT JOIN FETCH b.publisher p WHERE b.id = :id")
    Optional<Book> findByIdWithAllDetails(@Param("id") Long id);
}
