package com.example.libms;

import java.util.Date;

public class BookSuggestion extends Book {
    private String thumbnail;

    public BookSuggestion(String title, String author, String publisher, Date publishedDate, String isbn, String thumbnail) {
        super(title, author, publisher, publishedDate, isbn);
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}