package com.george.books_service.repository;

import com.george.books_service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByGoogleBookId(String googleBookId);
}
