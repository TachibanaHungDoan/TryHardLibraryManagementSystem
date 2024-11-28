package com.example.libms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GoogleBooksAPI {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
                JsonArray items = jsonResponse.getAsJsonArray("items");

                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        JsonObject book = items.get(i).getAsJsonObject();
                        JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                        // Lấy thông tin cơ bản
                        String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown";
                        String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").getAsJsonArray().get(0).getAsString() : "Unknown";

                        // Lấy ISBN
                        String isbn = "";
                        if (volumeInfo.has("industryIdentifiers")) {
                            isbn = volumeInfo.get("industryIdentifiers").getAsJsonArray().get(0).getAsJsonObject().get("identifier").getAsString();
                        }

                        // Lấy thumbnail
                        String thumbnail = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
                                ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString()
                                : null;

                        // Lấy publisher
                        String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown";

                        // Lấy publishedDate (chuyển sang kiểu Date)
                        Date publishedDate = null;
                        if (volumeInfo.has("publishedDate")) {
                            String publishedDateStr = volumeInfo.get("publishedDate").getAsString();
                            try {
                                publishedDate = DATE_FORMAT.parse(publishedDateStr);
                            } catch (ParseException e) {
                                // Nếu không parse được thì giữ null
                                publishedDate = null;
                            }
                        }

                        // Thêm vào danh sách gợi ý
                        suggestions.add(new BookSuggestion(title, authors, publisher, publishedDate,isbn, thumbnail));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
}