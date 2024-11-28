package com.example.libms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoogleBooksAPI {

    private static final SimpleDateFormat DATE_FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH = new SimpleDateFormat("yyyy-MM");
    private static final SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat DATE_FORMAT_MONTH_YEAR = new SimpleDateFormat("MMMM yyyy");
    private static final SimpleDateFormat DATE_FORMAT_DAY_MONTH_YEAR = new SimpleDateFormat("MMMM dd, yyyy");
    private static final int MAX_RESULTS = 3;

    public static List<BookSuggestion> searchBooks(String query) {

        List<BookSuggestion> suggestions = new ArrayList<>();
        String apiUrl = String.format("https://www.googleapis.com/books/v1/volumes?q=%s", query.replace(" ", "+"));
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Kiểm tra mã phản hồi
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                // Đọc kết quả JSON
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                // Phân tích JSON bằng Gson
                JsonObject jsonResponse = JsonParser.parseString(inline.toString()).getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonArray("docs");

                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        JsonObject book = items.get(i).getAsJsonObject();
                        // Lấy thông tin cơ bản
                        String title = book.has("title") ? book.get("title").getAsString() : "Unknown";
                        String authors = book.has("author_name") ? book.get("author_name").getAsJsonArray().get(0).getAsString() : "Unknown";

                        // Lấy ISBN
                        String isbn10 = null;
                        String isbn13 = null;
                        if (book.has("isbn")) {
                            JsonArray isbns = book.get("isbn").getAsJsonArray();
                            for (int j = 0; j < isbns.size(); j++) {
                                String isbn = isbns.get(j).getAsString();
                                if (isbn.length() == 10) {
                                    isbn10 = isbn;
                                } else if (isbn.length() == 13) {
                                    isbn13 = isbn;
                                }
                            }
                        }

                        String thumbnail = book.has("cover_i") ? String.format("https://covers.openlibrary.org/b/id/%s-L.jpg",
                                book.get("cover_i").getAsString()) : null;

                        // Lấy publisher
                        String publisher = book.has("publisher") ? book.get("publisher").getAsJsonArray().get(0).getAsString() : "Unknown";

                        // Lấy publishedDate (chuyển sang kiểu Date)
                        Date publishedDate = null;
                        if (book.has("publish_date")) {
                            String publishedDateStr = book.get("publish_date").getAsJsonArray().get(0).getAsString();
                            try {
                                if (publishedDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                    publishedDate = DATE_FORMAT_FULL.parse(publishedDateStr);
                                } else if (publishedDateStr.matches("\\d{4}-\\d")) {
                                    publishedDate = DATE_FORMAT_YEAR_MONTH.parse(publishedDateStr);
                                } else if (publishedDateStr.matches("\\d{4}")) {
                                    publishedDate = DATE_FORMAT_YEAR.parse(publishedDateStr);
                                } else if (publishedDateStr.matches("[A-Za-z]+ \\d{4}")) {
                                    publishedDate = DATE_FORMAT_MONTH_YEAR.parse(publishedDateStr);
                                } else if (publishedDateStr.matches("[A-Za-z]+ \\d{1,2}, \\d{4}")) {
                                    publishedDate = DATE_FORMAT_DAY_MONTH_YEAR.parse(publishedDateStr);
                                } else {
                                    System.out.println("Invalid date format: " + publishedDateStr);
                                }
                                //System.out.println("Retrieve from JSON API: " + publishedDateStr + " >>>>>> after DATE_FORMAT: " + publishedDate);
                            } catch (ParseException e) {
                                //System.out.println("ParseException: " + e.getMessage());
                                publishedDate = null;
                            }
                        }
                        String isbn = (isbn13 != null) ? isbn13 : (isbn10 != null ? isbn10 : "");
                        // Thêm vào danh sách gợi ý
                        suggestions.add(new BookSuggestion(title, authors, publisher, publishedDate, isbn, thumbnail));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
}