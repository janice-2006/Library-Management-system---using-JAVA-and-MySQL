package com.library.system;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    // 1. Method to get all books for the table
    @GetMapping("/all")
    public List<Map<String, String>> getAllBooks() {
        List<Map<String, String>> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> book = new HashMap<>();
                book.put("id", rs.getString("book_id"));
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                book.put("status", rs.getString("status"));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    // 2. Method to add a new book
    @PostMapping("/add")
    public String addBook(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String author = payload.get("author");

        String sql = "INSERT INTO Books (title, author, status) VALUES (?, ?, 'Available')";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();

            return "Book added successfully!";
        } catch (Exception e) {
            return "Error adding book: " + e.getMessage();
        }
    }
}
