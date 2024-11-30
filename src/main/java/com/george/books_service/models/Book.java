package com.george.books_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity // Indicates that this class is a JPA entity
public class Book {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies that the ID should be auto-generated
    private Long id;

    @NotBlank // Ensures that the title cannot be null or empty
    private String title;

    @NotBlank // Ensures that the author cannot be null or empty
    private String author;

    @NotBlank // Ensures that the description cannot be null or empty
    private String description;

    private String publishedDate; // Field for the book's publication date

    @NotBlank // Ensures that the Google Book ID cannot be null or empty
    private String googleBookId;

    // Default constructor
    public Book() {}

    // Constructor with parameters for easy instantiation
    public Book(String title, String author, String description, String publishedDate, String googleBookId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishedDate = publishedDate;
        this.googleBookId = googleBookId;
    }

    // Getters and Setters for each field
    public Long getId() {
        return id; // Returns the book ID
    }

    public void setId(Long id) {
        this.id = id; // Sets the book ID
    }

    public String getTitle() {
        return title; // Returns the book title
    }

    public void setTitle(String title) {
        this.title = title; // Sets the book title
    }

    public String getAuthor() {
        return author; // Returns the book author
    }

    public void setAuthor(String author) {
        this.author = author; // Sets the book author
    }

    public String getDescription() {
        return description; // Returns the book description
    }

    public void setDescription(String description) {
        this.description = description; // Sets the book description
    }

    public String getPublishedDate() {
        return publishedDate; // Returns the book's published date
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate; // Sets the book's published date
    }

    public String getGoogleBookId() {
        return googleBookId; // Returns the Google Book ID
    }

    public void setGoogleBookId(String googleBookId) {
        this.googleBookId = googleBookId; // Sets the Google Book ID
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", googleBookId='" + googleBookId + '\'' +
                '}'; // Provides a string representation of the book object
    }
}
