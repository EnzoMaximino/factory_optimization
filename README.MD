# Factory OS

Factory OS is a fullstack production optimization system built with Spring Boot and Vue 3.

The system allows:

- Raw Material management (CRUD)
- Product management with required composition
- Production calculation based on available stock
- Automatic stock update after production execution

---

## Technologies Used

Backend:
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Gradle

Frontend:
- Vue 3
- Vite
- JavaScript

---

## How to Run the Project Locally

### 1. Requirements

- Java 21+
- Node.js 18+
- PostgreSQL running locally

---

### 2. Configure Database

Create a PostgreSQL database and update the credentials in:

src/main/resources/application.properties

---

### 3. Build Frontend

Go to the frontend folder:

cd frontend/factory-frontend

Install dependencies:

npm install

Build the frontend:

npm run build

---

### 4. Run Backend

From the root folder of the project:

On Windows:
gradlew.bat bootRun

On Mac/Linux:
./gradlew bootRun

---

### 5. Access Application

Open in your browser:

http://localhost:8080

---

## Project Structure

- /src → Spring Boot backend
- /frontend → Vue frontend source code
- /src/main/resources/static → Built frontend (served by Spring Boot)

---

## Notes

The frontend is served directly by Spring Boot after running `npm run build`.
