
# Leave Management System

This repository contains a Dockerized Leave Management System application. Follow the instructions below to set up and test the application using Docker Compose.

---

## Prerequisites

- Docker and Docker Compose installed on your machine.
- Access to:
  - `docker-compose.prod.yml` and a `.env` file (for production setup)
  - or, the repository and config files if building from source.

---

## Recommended Setup (Using `docker-compose.prod.yml`)

1. **Prepare Directory Structure:**
   ```bash
   mkdir ist
   cd ist/
   ```
   - Place the `docker-compose.prod.yml` file inside this directory.
   - Create a `leaveManagement_FE` folder:
     ```bash
     mkdir leaveManagement_FE
     cd leaveManagement_FE
     ```
   - Paste the provided `.env` file here.

2. **Run the Application:**
   ```bash
   docker-compose -f docker-compose.prod.yml down
   docker-compose -f docker-compose.prod.yml up --build
   ```

3. **Access the App:**
   - Open your browser and visit: `http://localhost:8080`
   - Open your browser and visit: `http://localhost:8081/swagger-ui.html`


---

## Alternative: Build from Cloned Repositories

1. **Set Up Working Directory:**
   ```bash
   mkdir ist
   cd ist
   ```

2. **Clone the Repositories:**
   ```bash
   git clone https://github.com/nahimanajz/leaveManagement_FE.git
   cd leaveManagement_FE
   cp .env.example .env  # Fill in required environment variables
   cd ..
   git clone https://github.com/nahimanajz/LeaveManagementSystem.git
   ```

3. **Copy Configuration Files:**
   - Copy `docker-compose.yml` to the working directory.
   - Copy `application.properties` from:
     ```
     LeaveManagementSystem/src/main/resources/application.properties.example
     ```
   - To:
   ```
     LeaveManagementSystem/src/main/resources/application.properties
     ```


4. **Update Database Connection:**
   - In `application.properties`, update:
     ```properties
     spring.datasource.url=jdbc:postgresql://db:5432/lms4
     ```

5. **Resolve Port Conflicts (if any):**
   ```bash
   sudo lsof -i :5432
   sudo kill <process_id>
   ```

6. **Run the Application:**
   ```bash
   docker-compose down
   docker-compose up --build
   ```

7. **Access the App:**
   - Go to: `http://localhost:8080`
   - Go to: `http://localhost:8080/swagger-ui.html` for swagger documentation

---

## Configuration Details

- **Database**
  - URL: `jdbc:postgresql://db:5432/lms4`
  - Username: `postgres`
  - Password: `123123`
  
- **JWT**
  - Secret: `A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6A7B8C9D0E1F2`
  - Expiration: `3600000` milliseconds

---

## Notes

- Ensure `application.properties` contains the correct DB and email configurations.
- PostgreSQL should be accessible at the configured host and port before starting the application.

---
## Point to Improve
- This application has ability to sign in using microsoft account along with admin and manager signup form
Users can perform almost everything mentioned in challenge description, given time I would work on code quality in order to improve on frontend I have used
- Add email notification to support inAppNotification which is currently being used 
## AI TOOLS
- codium ai agent
- github copilot vs code extension
- Cursor code editor
- Lovable for Intuitive UI privion
## Technologies used
 - Spring framework,
 - Java,
 - React-query for caching data,
 - ReactJs,
 - Typescript
## Contact

For support or inquiries, reach out to the project maintainer. jazzonahimana@gmail.com or +250785343588

