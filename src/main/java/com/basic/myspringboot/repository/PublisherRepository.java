package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    @Query("SELECT p FROM Publisher p LEFT JOIN FETCH p.books WHERE p.id = :id")
    Optional<Publisher> findByIdWithBooks(@Param("id") Long id);

    @Query("SELECT p FROM Publisher p LEFT JOIN FETCH p.books WHERE p.name = :name")
    Optional<Publisher> findByNameWithBooks(@Param("name") String name);

    boolean existsByName(String name);
}
