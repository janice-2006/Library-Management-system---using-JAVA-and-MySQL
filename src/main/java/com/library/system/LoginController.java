package com.library.system;

import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");

        // Problem Rectified: Use try-with-resources to automatically close Connection
        // and PreparedStatement
        // Problem Rectified: Avoided "SELECT *" by specifying the columns needed (id)
        String sql = "SELECT student_id FROM Students WHERE name = ? AND email = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "Login Successful! Welcome " + name;
                } else {
                    return "Invalid name or email.";
                }
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
