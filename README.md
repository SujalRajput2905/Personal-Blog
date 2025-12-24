# Personal Blog Application

A full-stack personal blog application built with **Spring Boot**, **Spring Security (JWT)**, and **MongoDB**, featuring role-based access control, an admin dashboard, and an Instagram-style like system.

**Live Demo:**  
https://personal-blog-uogy.onrender.com/login.html
---

## 🚀 Features

### 🔐 Authentication & Security
- JWT-based authentication
- Secure password hashing
- Role-based authorization (ADMIN / USER)

### 📝 Blog Management
- Create, read, update posts
- Admin-only post creation & editing
- Author/admin validation on updates

### ❤️ Like System
- Instagram-style like / unlike
- One like per user per post
- Persistent like counts

### 🎨 Frontend
- Modern login & register pages
- Clean posts feed UI
- Admin dashboard
- Built using **HTML, CSS, JavaScript (Vanilla)**

---

## 🛠 Tech Stack

**Backend**
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- MongoDB (Atlas)
- Maven

**Frontend**
- HTML
- CSS
- JavaScript

---

## 📸 Screenshots

### Login
<img width="1919" height="735" alt="login" src="https://github.com/user-attachments/assets/33d99cee-9f24-43ee-beaf-d679c43e20bc" />

### Register
<img width="1919" height="717" alt="register" src="https://github.com/user-attachments/assets/68f85c70-b752-42ee-b60e-51bdba7a8ee4" />

### Posts
<img width="1919" height="785" alt="posts" src="https://github.com/user-attachments/assets/55161560-0c6b-47df-b40c-10bfe6326872" />

### Admin Panel
<img width="1898" height="868" alt="admin" src="https://github.com/user-attachments/assets/261493b2-f9e8-44ba-9648-2dbcc71c6e6c" />

---

## ⚙️ Environment Variables

Before running the project, set the following environment variables:

```env
MONGO_URI=your_mongodb_connection_string
JWT_SECRET=your_jwt_secret_key
