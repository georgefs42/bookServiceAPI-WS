package com.george.books_service.service;

import com.george.books_service.entity.Book;
import com.george.books_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class GoogleBooksService {
    @Value("${google.api.key}")
    private String apiKey;

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public GoogleBooksService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book searchBookByTitle(String title) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + title + "&key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);

        JSONObject jsonResponse = new JSONObject(response);
        JSONObject bookInfo = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");

        Book book = new Book();
        book.setGoogleBookId(jsonResponse.getJSONArray("items").getJSONObject(0).getString("id"));
        book.setTitle(bookInfo.getString("title"));
        book.setAuthor(bookInfo.getJSONArray("authors").getString(0));
        book.setPublishedDate(bookInfo.getString("publishedDate"));
        book.setDescription(bookInfo.getString("description"));

        return book;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
}
