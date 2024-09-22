package com.george.books_service.service;

import com.george.books_service.entity.Book;
import com.george.books_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service // Indicates that this class is a service component in the Spring context
public class GoogleBooksService {
    @Value("${google.api.key}") // Injects the Google API key from application properties
    private String apiKey;

    private final BookRepository bookRepository; // Repository for database operations
    private final RestTemplate restTemplate = new RestTemplate(); // RestTemplate for making HTTP requests

    // Constructor to initialize the repository
    public GoogleBooksService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Method to search for a book by its title using the Google Books API
    public Book searchBookByTitle(String title) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + title + "&key=" + apiKey; // Construct API URL
        String response = restTemplate.getForObject(url, String.class); // Make GET request to the API

        JSONObject jsonResponse = new JSONObject(response); // Parse the response to JSON
        JSONObject bookInfo = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo"); // Extract book information

        Book book = new Book(); // Create a new Book object
        // Set properties from the API response
        book.setGoogleBookId(jsonResponse.getJSONArray("items").getJSONObject(0).getString("id"));
        book.setTitle(bookInfo.getString("title"));
        book.setAuthor(bookInfo.getJSONArray("authors").getString(0)); // Assuming the first author is used
        book.setPublishedDate(bookInfo.getString("publishedDate"));
        book.setDescription(bookInfo.getString("description"));

        return book; // Return the populated Book object
    }

    // Method to save a book to the database
    public Book saveBook(Book book) {
        return bookRepository.save(book); // Save the book and return the saved entity
    }
}
