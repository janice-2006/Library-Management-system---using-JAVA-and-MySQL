package com.library.system;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @PostMapping("/add")
    public String addStudent(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");

        String sql = "INSERT INTO students (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return "Student registered successfully!";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/all")
    public List<Map<String, String>> getAllStudents() {
        List<Map<String, String>> students = new ArrayList<>();
        String sql = "SELECT student_id, name FROM students";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> student = new HashMap<>();
                student.put("id", rs.getString("student_id"));
                student.put("name", rs.getString("name"));
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}