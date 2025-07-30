## AuthenticationService

### Purpose

This microservice handles **user registration and login** with basic credential checks. It is used by the frontend to allow access to protected parts of the system.

---

### Input / Output

| Action   | Endpoint                  | Input (JSON)                             | Output (JSON)                                                                                                   |
| -------- | ------------------------- | ---------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Register | `POST /api/auth/register` | `{ "name": "user", "password": "pass" }` | `{ "message": "User registered" }` or `{ "error": "Username already exists" }`                                  |
| Login    | `POST /api/auth/login`    | `{ "name": "user", "password": "pass" }` | `{ "message": "Login successful", "user": { "id": ..., "name": ... } }` or `{ "error": "Invalid credentials" }` |

---

### File Roles & Flow

* **`TraineeApplication.java`**

  * Bootstraps the Spring Boot application.

* **`LoginController.java`**

  * Defines the `/register` and `/login` endpoints.
  * Accepts JSON payload and delegates logic to `UserService`.

* **`UserService.java`**

  * Contains logic to:

    * Register user if name is unique
    * Validate login credentials
  * Uses `UserRepository` to query/save in DB.

* **`UserRepository.java`**

  * JPA interface to query `users` table.
  * Provides `findByName()` and `existsByName()`.

* **`User.java`**

  * Entity mapped to `users` table.
  * Fields: `id`, `name`, `password`, `createdAt`.

---

### Database Storage

* **Table:** `users`
* **Stored Fields:**

  * `id` (auto-generated)
  * `name` (unique, required)
  * `password` (stored as plain text for now)
  * `createdAt` (timestamp)

---

### Summary of Connections

```
Frontend <--> LoginController <--> UserService <--> UserRepository <--> PostgreSQL
```

---