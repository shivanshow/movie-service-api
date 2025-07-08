# 🎬 MOVIE SERVICE API

## 🔹 PROJECT OVERVIEW

**MOVIE SERVICE API** is a RESTful web service built using **Spring Boot**.  
It provides endpoints to manage a collection of movies, including operations such as:

- **Add a new movie with functionality of uploading files**
- **Get all movies (paginated)**
- **Get all movies (sorted)**
- **Get a movie by ID**
- **Update a movie**
- **Delete a movie**

The API also supports file upload functionality using `MultipartFile`, and serves stored poster images via HTTP.

## 🔹 TECH STACK & REQUIREMENTS

### BACKEND
- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **Hibernate**
- **Jakarta Validation**
- **MySQL Database**

### BUILD TOOL
- **Maven**

### IDE
- Developed using **IntelliJ IDEA**

### OTHER TOOLS
- **Postman** for testing the API
- **Git** for version control

## 🔹 MOVIE CONTROLLER ENDPOINTS

All endpoints are prefixed with: **`/api/v1/movie`**

### ADD A MOVIE

**POST** `/add-movie`

- **Description:** Adds a new movie along with its poster image.
- **Consumes:** `multipart/form-data`
- **Request Parts:**
  - `file`: The poster image file (required)
  - `movieDTO`: A JSON string of movie details
- **Returns:** `201 Created` with the saved `MovieDTO`
- **Errors:** Throws `EmptyFileException` if file is not provided.

---

### GET A MOVIE BY ID

**GET** `/{movieId}`

- **Description:** Retrieves a movie by its unique ID.
- **Returns:** `200 OK` with the `MovieDTO` for the given ID

---

### GET ALL MOVIES (PAGINATED)

**GET** `/all`

- **Description:** Returns paginated movie results.
- **Query Parameters:**
  - `pageNumber` (default: `0`)
  - `pageSize` (default: `5`)
- **Returns:** A `MoviePageResponse` containing movies and pagination metadata.

---

### GET ALL MOVIES (SORTED + PAGINATED)

**GET** `/all-Sorted`

- **Description:** Returns paginated & sorted movies.
- **Query Parameters:**
  - `pageNumber` (default: `0`)
  - `pageSize` (default: `5`)
  - `sortBy` (default: `movieId`)
  - `sortDir` (default: `asc` or `desc`)
- **Returns:** A sorted `MoviePageResponse`

---

### UPDATE A MOVIE

**PUT** `/update-movie/{movieId}`

- **Description:** Updates an existing movie's details and optionally its poster.
- **Consumes:** `multipart/form-data`
- **Request Parts:**
  - `file`: New poster image (optional)
  - `movieDTO`: Updated movie details
- **Returns:** `201 Created` with the updated `MovieDTO`

---

### DELETE A MOVIE

**DELETE** `/delete/{movieId}`

- **Description:** Deletes the movie and its associated poster image.
- **Returns:** `200 OK` with a confirmation message

---

