package com.george.books_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String googleBookId;
    private String title;
    private String author;
    private String description;
    private String publishedDate;
}
