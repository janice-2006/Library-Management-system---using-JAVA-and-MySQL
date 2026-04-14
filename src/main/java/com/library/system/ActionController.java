package com.library.system;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.Map;

@RestController
@RequestMapping("/api/action")
public class ActionController {

    /**
     * Handles the borrowing of a book.
     * Updates the book status and records the transaction in the borrowing table.
     */
    @PostMapping("/borrow")
    public String borrowBook(@RequestBody Map<String, Object> payload) {
        // Extracting IDs from the request payload
        int studentId = Integer.parseInt(payload.get("studentId").toString());
        int bookId = Integer.parseInt(payload.get("bookId").toString());

        String checkSql = "SELECT status FROM Books WHERE book_id = ?";
        String updateBookSql = "UPDATE Books SET status = 'Borrowed' WHERE book_id = ?";
        String insertBorrowSql = "INSERT INTO borrowing (student_id, book_id, borrow_date) VALUES (?, ?, CURRENT_DATE)";

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Check if the book exists and is available
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("status");
                        if (!"Available".equalsIgnoreCase(status)) {
                            return "Error: This book is already borrowed.";
                        }
                    } else {
                        return "Error: Book ID " + bookId + " not found.";
                    }
                }
            }

            // 2. Perform updates using a transaction to ensure data integrity
            conn.setAutoCommit(false);
            try {
                // Update Book Status
                try (PreparedStatement updateStmt = conn.prepareStatement(updateBookSql)) {
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }

                // Record Transaction
                try (PreparedStatement borrowStmt = conn.prepareStatement(insertBorrowSql)) {
                    borrowStmt.setInt(1, studentId);
                    borrowStmt.setInt(2, bookId);
                    borrowStmt.executeUpdate();
                }

                conn.commit();
                return "Book issued successfully!";
            } catch (SQLException e) {
                conn.rollback();
                return "Transaction failed: " + e.getMessage();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    /**
     * Handles the returning of a book.
     * Sets the book status back to 'Available' and updates the return date.
     */
    @PostMapping("/return")
    public String returnBook(@RequestBody Map<String, Object> payload) {
        int bookId = Integer.parseInt(payload.get("bookId").toString());

        String updateBookSql = "UPDATE Books SET status = 'Available' WHERE book_id = ?";
        String updateBorrowSql = "UPDATE borrowing SET return_date = CURRENT_DATE WHERE book_id = ? AND return_date IS NULL";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Update Book Status
                try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {
                    updateBookStmt.setInt(1, bookId);
                    int rows = updateBookStmt.executeUpdate();
                    if (rows == 0)
                        return "Error: Book ID not found.";
                }

                // 2. Mark Return Date in borrowing history
                try (PreparedStatement updateBorrowStmt = conn.prepareStatement(updateBorrowSql)) {
                    updateBorrowStmt.setInt(1, bookId);
                    updateBorrowStmt.executeUpdate();
                }

                conn.commit();
                return "Book returned successfully!";
            } catch (SQLException e) {
                conn.rollback();
                return "Return failed: " + e.getMessage();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }
}