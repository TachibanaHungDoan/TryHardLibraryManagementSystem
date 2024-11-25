package com.example.libms;

import java.util.Date;

public class BookSuggestion {

    private String title;
    private String author;
    private String publisher;
    private Date publishedDate;
    private String isbn;
    private String thumbnail;

    public BookSuggestion(String title, String author, String publisher, Date publishedDate, String isbn, String thumbnail) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}