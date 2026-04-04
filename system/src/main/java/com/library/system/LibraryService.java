package com.library.system;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class LibraryService {

    // 1. Add Book Logic (Cleaned of Scanner)
    public String addBook(int id, String title, String author) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Books (book_id, title, author, available) VALUES (?, ?, ?, TRUE)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.executeUpdate();
            return "Book added successfully!";
        } catch (Exception e) {
            return "Error adding book: " + e.getMessage();
        }
    }

    // 2. View All Books Logic (Now returns a List for the UI)
    public List<Map<String, Object>> getAllBooks() {
        List<Map<String, Object>> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books");
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("id", rs.getInt("book_id"));
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                book.put("available", rs.getBoolean("available"));
                books.add(book);
            }
        } catch (Exception e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }

    // 6. Search Book Logic
    public List<Map<String, Object>> searchBook(String query, boolean isIdSearch) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = isIdSearch ? "SELECT * FROM Books WHERE book_id = ?"
                    : "SELECT * FROM Books WHERE title LIKE ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (isIdSearch) {
                pstmt.setInt(1, Integer.parseInt(query));
            } else {
                pstmt.setString(1, "%" + query + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("id", rs.getInt(1));
                book.put("title", rs.getString(2));
                book.put("status", rs.getBoolean(4) ? "Available" : "Issued");
                results.add(book);
            }
        } catch (Exception e) {
            System.err.println("Error searching: " + e.getMessage());
        }
        return results;
    }
}
