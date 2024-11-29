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
    private static final int MAX_RESULTS = 3;

    public static List<BookSuggestion> searchBooks(String query) {

        List<BookSuggestion> suggestions = new ArrayList<>();
        String apiUrl = String.format("https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=%d", query.replace(" ", "+"), MAX_RESULTS);
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
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                // Phân tích JSON
                JsonObject jsonResponse = JsonParser.parseString(inline.toString()).getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonArray("items");

                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        JsonObject book = items.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");

                        // Lấy thông tin cơ bản
                        String title = book.has("title") ? book.get("title").getAsString() : "Unknown";
                        String authors = book.has("authors") ? book.get("authors").getAsJsonArray().get(0).getAsString() : "Unknown";

                        // Lấy ISBN
                        String isbn10 = null;
                        String isbn13 = null;
                        if (book.has("industryIdentifiers")) {
                            JsonArray isbns = book.getAsJsonArray("industryIdentifiers");
                            for (int j = 0; j < isbns.size(); j++) {
                                JsonObject isbnObj = isbns.get(j).getAsJsonObject();
                                String type = isbnObj.get("type").getAsString();
                                String identifier = isbnObj.get("identifier").getAsString();
                                if ("ISBN_10".equals(type)) {
                                    isbn10 = identifier;
                                } else if ("ISBN_13".equals(type)) {
                                    isbn13 = identifier;
                                }
                            }
                        }

                        String thumbnail = book.has("imageLinks") ? book.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : null;

                        // Lấy publisher
                        String publisher = book.has("publisher") ? book.get("publisher").getAsString() : "Unknown";

                        // Lấy publishedDate (chuyển sang kiểu Date)
                        Date publishedDate = null;
                        if (book.has("publishedDate")) {
                            String publishedDateStr = book.get("publishedDate").getAsString();
                            try {
                                if (publishedDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                    publishedDate = DATE_FORMAT_FULL.parse(publishedDateStr);
                                } else if (publishedDateStr.matches("\\d{4}-\\d{2}")) {
                                    publishedDate = DATE_FORMAT_YEAR_MONTH.parse(publishedDateStr);
                                } else if (publishedDateStr.matches("\\d{4}")) {
                                    publishedDate = DATE_FORMAT_YEAR.parse(publishedDateStr);
                                }
                            } catch (ParseException e) {
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