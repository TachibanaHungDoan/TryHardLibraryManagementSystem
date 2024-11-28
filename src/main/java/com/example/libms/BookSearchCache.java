package com.example.libms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSearchCache {
    private static final Map<String, List<BookSuggestion>> cache = new HashMap<>();

    public static List<BookSuggestion> get(String query) {
        return cache.get(query);
    }

    public static void put(String query, List<BookSuggestion> suggestions) {
        cache.put(query, suggestions);
    }
}
