package com.george.books_service.repository;

import com.george.books_service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository; // Importing JpaRepository for basic CRUD operations
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Indicates that this interface is a Spring Data repository
public interface BookRepository extends JpaRepository<Book, Long> { // Extends JpaRepository to provide CRUD methods for Book entity
    // Method to find a book by its Google Book ID
    Optional<Book> findByGoogleBookId(String googleBookId);
}
