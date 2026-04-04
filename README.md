📚Library Management System (LMS)
Project Overview
This is a full-stack web application designed to digitize and automate core library operations. Unlike basic CRUD tutorials, this system implements a relational database architecture to manage complex interactions between books and students. The project focuses on high performance, data integrity, and a seamless user experience (UX) through asynchronous communication and live-filtering.

🛠️ Technical Architecture
1. Backend: Java Spring Boot
The core logic is powered by Spring Boot, utilizing RESTful APIs to bridge the gap between the frontend and the database.

Controller Layer: Implemented specialized controllers (BookController, StudentController, ActionController) to maintain a clean Separation of Concerns.

Connection Management: Utilized the Try-with-Resources pattern to ensure database connections are closed automatically, preventing memory leaks—a critical practice in enterprise Java development.

Transactional Logic: The borrowing system uses SQL Transactions to ensure that a book's status is only updated if the borrowing record is successfully created, maintaining absolute data consistency.

2. Database: MySQL
A relational schema designed for scalability:

Foreign Key Constraints: Establishes rigid links between students, books, and borrowing tables to prevent "orphan records."

Auto-Increment Logic: Configured primary keys to handle unique ID generation automatically, reducing manual input errors.

3. Frontend: Modern JavaScript & CSS3
The UI was built with a focus on "Zero-Reload" interactions:

Asynchronous Fetch API: All data updates (Add, Borrow, Register) happen in the background without refreshing the page.

Live Search Engine: Implemented a client-side search algorithm that filters thousands of records instantly using JavaScript's .includes() and .toUpperCase() methods.

Responsive Grid Layout: A CSS Grid-based dashboard that adapts to different screen sizes, from desktop monitors to mobile tablets.

🌟 Key Problem-Solving Milestones
During development, I encountered and resolved several real-world engineering challenges:

Constraint Conflicts: Resolved FOREIGN KEY update errors by strategically managing the order of ALTER TABLE operations.

Input Validation: Fixed the "Field 'email' doesn't have a default value" error by optimizing the database schema to handle optional fields, making the registration flow more flexible.

State Management: Built a synchronized loading system (loadStudents) that updates multiple UI components (the Borrow Dropdown and the Student Directory) from a single backend call.

🚀 How to Run Locally
Clone the Repository: git clone [Your-Repo-Link]

Setup Database: Execute the provided schema.sql in your MySQL terminal.

Configure Properties: Update application.properties with your MySQL credentials.

Build & Run: Use Maven to build the project and run it on localhost:8081.

Target Audience
This project serves as a practical implementation of Database Management Systems (DBMS) and Web Technologies coursework, specifically tailored for academic library environments like SRMIST.
