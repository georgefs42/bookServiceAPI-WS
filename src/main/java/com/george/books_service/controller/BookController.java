package com.george.books_service.controller;

import com.george.books_service.entity.Book;
import com.george.books_service.service.GoogleBooksService;
import com.george.books_service.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books") // Base URL for all book-related endpoints
public class BookController {
    private final GoogleBooksService googleBooksService; // Service to interact with Google Books API
    private final BookRepository bookRepository; // Repository for database operations

    // Constructor to initialize the services and repository
    public BookController(GoogleBooksService googleBooksService, BookRepository bookRepository) {
        this.googleBooksService = googleBooksService;
        this.bookRepository = bookRepository;
    }

    // Get all books from the database
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll(); // Retrieve all books
        return ResponseEntity.ok(books); // Return 200 OK with the list of books
    }

    // Search for a book by its title
    @GetMapping("/search")
    public ResponseEntity<Book> searchBook(@RequestParam String title) {
        Book book = googleBooksService.searchBookByTitle(title); // Use service to search for the book
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build(); // Return found book or 404
    }

    // Add a new book to the database
    @PostMapping
    public ResponseEntity<Book> addBookToDatabase(@RequestBody @Valid Book book) {
        Optional<Book> existingBook = bookRepository.findByGoogleBookId(book.getGoogleBookId()); // Check if the book already exists
        if (existingBook.isPresent()) {
            return ResponseEntity.badRequest().build(); // Return 400 if the book already exists
        }

        Book savedBook = bookRepository.save(book); // Save the new book to the database
        return ResponseEntity.ok(savedBook); // Return 200 OK with the saved book
    }

    // Get a book by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id) // Find the book by ID
                .map(ResponseEntity::ok) // If found, return it with 200 OK
                .orElse(ResponseEntity.notFound().build()); // Otherwise, return 404
    }

    // Update an existing book's details
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid Book bookDetails) {
        return bookRepository.findById(id) // Find the book by ID
                .map(book -> {
                    // Update book details
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setDescription(bookDetails.getDescription());
                    book.setPublishedDate(bookDetails.getPublishedDate());
                    return ResponseEntity.ok(bookRepository.save(book)); // Save and return updated book
                })
                .orElse(ResponseEntity.notFound().build()); // Return 404 if the book is not found
    }

    // Delete a book by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id) // Find the book by ID
                .map(book -> {
                    bookRepository.delete(book); // Delete the book
                    return ResponseEntity.noContent().build(); // Return 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // Return 404 if the book is not found
    }
}
