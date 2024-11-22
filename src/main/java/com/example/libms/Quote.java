package com.example.libms;

public class Quote {
        public String getQuote() {
            return quote;
        }

        public String getAuthor() {
            return author;
        }

        private final String quote;
        private final String author;

        public Quote(String quote, String author) {
            this.quote = quote;
            this.author = author;
        }
}
