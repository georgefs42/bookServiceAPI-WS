package com.george.books_service.controller;

import com.george.books_service.entity.Book;
import com.george.books_service.service.GoogleBooksService;
import com.george.books_service.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    private final GoogleBooksService googleBooksService;
    private final BookRepository bookRepository;

    public BookController(GoogleBooksService googleBooksService, BookRepository bookRepository) {
        this.googleBooksService = googleBooksService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<Book> searchBook(@RequestParam String title) {
        Book book = googleBooksService.searchBookByTitle(title);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<Book> addBookToDatabase(@RequestBody Book book) {
        Book savedBook = googleBooksService.saveBook(book);
        return ResponseEntity.ok(savedBook);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
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
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
