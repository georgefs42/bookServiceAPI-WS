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
@RequestMapping("/books")
public class BookController {
    private final GoogleBooksService googleBooksService;
    private final BookRepository bookRepository;

    public BookController(GoogleBooksService googleBooksService, BookRepository bookRepository) {
        this.googleBooksService = googleBooksService;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<Book> searchBook(@RequestParam String title) {
        Book book = googleBooksService.searchBookByTitle(title);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Book> addBookToDatabase(@RequestBody @Valid Book book) {
        Optional<Book> existingBook = bookRepository.findByGoogleBookId(book.getGoogleBookId());
        if (existingBook.isPresent()) {
            return ResponseEntity.badRequest().build(); // Return 400 if the book already exists
        }

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook); // Return 200 with the saved book
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setDescription(bookDetails.getDescription());
                    book.setPublishedDate(bookDetails.getPublishedDate());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
