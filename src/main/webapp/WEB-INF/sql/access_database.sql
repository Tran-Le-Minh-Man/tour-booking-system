-- ============================================================
-- SQL Script for Microsoft Access Database (ACDB)
-- Run this in Microsoft Access to create tables
-- ============================================================

-- Lưu ý: Access không hỗ trợ tạo table bằng SQL thuần
-- Bạn cần tạo table trong Microsoft Access với thiết kế sau:
-- ============================================================

-- TABLE: Users
-- ============================================================
-- Field Name          | Data Type      | Size/Format | Required | Primary Key
-- -----------------------------------------------------------------------
-- id                  | AutoNumber     | Long Integer| Yes      | Yes (Primary Key)
-- email               | Text           | 100         | Yes      | Yes (No Duplicates)
-- password_hash       | Text           | 255         | Yes      | No
-- full_name           | Text           | 100         | Yes      | No
-- phone               | Text           | 20          | No       | No
-- role                | Text           | 20          | No       | No (Default: "USER")
-- created_at          | Date/Time      |             | No       | No

-- TABLE: Tours
-- ============================================================
-- Field Name          | Data Type      | Size/Format | Required | Primary Key
-- -----------------------------------------------------------------------
-- id                  | AutoNumber     | Long Integer| Yes      | Yes (Primary Key)
-- name                | Text           | 200         | Yes      | No
-- description         | Memo           |             | No       | No
-- destination         | Text           | 100         | No       | No
-- departure_date      | Date/Time      |             | No       | No
-- duration            | Number         | Integer     | No       | No
-- price               | Currency       |             | No       | No
-- max_participants    | Number         | Integer     | No       | No (Default: 30)
-- current_participants| Number         | Integer     | No       | No (Default: 0)
-- image_url           | Text           | 255         | No       | No
-- status              | Text           | 20          | No       | No (Default: "ACTIVE")

-- TABLE: Bookings
-- ============================================================
-- Field Name          | Data Type      | Size/Format | Required | Primary Key
-- -----------------------------------------------------------------------
-- id                  | AutoNumber     | Long Integer| Yes      | Yes (Primary Key)
-- user_id             | Number         | Long Integer| Yes      | No
-- tour_id             | Number         | Long Integer| Yes      | No
-- booking_date        | Date/Time      |             | No       | No (Default: Now())
-- status              | Text           | 20          | No       | No (Default: "PENDING")
-- num_participants    | Number         | Integer     | No       | No (Default: 1)
-- total_price         | Currency       |             | No       | No
-- notes               | Memo           |             | No       | No

-- TABLE: Reviews
-- ============================================================
-- Field Name          | Data Type      | Size/Format | Required | Primary Key
-- -----------------------------------------------------------------------
-- id                  | AutoNumber     | Long Integer| Yes      | Yes (Primary Key)
-- user_id             | Number         | Long Integer| Yes      | No
-- tour_id             | Number         | Long Integer| Yes      | No
-- rating              | Number         | Integer     | Yes      | No
-- comment             | Memo           |             | No       | No

-- ============================================================
-- Relationship (Enforce Referential Integrity)
-- ============================================================
-- Bookings.user_id -> Users.id (Cascade Delete)
-- Bookings.tour_id -> Tours.id (Cascade Delete)
-- Reviews.user_id -> Users.id (Cascade Delete)
-- Reviews.tour_id -> Tours.id (Cascade Delete)

-- ============================================================
-- Sample Data for Users Table
-- ============================================================
-- Insert this SQL vào Access Query Design (SQL View):
-- INSERT INTO Users (email, password_hash, full_name, phone, role) 
-- VALUES ('admin@tour.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.E5fD4l.1S1p3V5U2W2', 'Admin', '0123456789', 'ADMIN');
-- INSERT INTO Users (email, password_hash, full_name, phone, role) 
-- VALUES ('user@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Nguyen Van A', '0901234567', 'USER');

-- Lưu ý: Password hash được tạo bằng BCrypt
-- Mật khẩu mặc định: 'admin123' cho admin, 'user123' cho user