# Client Accounting

This is not a big application for accounting of clients in the sphere of detailing services for cars.

The application provides information about the client and his/her appointments for services.

## Requirements

To successfully run and develop this project, your system needs to meet the following requirements:

### 1.**Java**
> - **Version**: 17

### 2. **Build Tool**
> - **Gradle**: Version 8.2.1 or higher

### 3. **Database**
> - **PostgreSQL**: Version 16.2 or higher
> - Make sure you set the proper database connection credentials in `application.properties` 

### 4. **Operating System**
> - **Windows**: Supported on Windows 10 or higher

### 5. **Docker**
> - **Docker**: Version 24.0.7 or higher

### 6. **Environment Variables**
> - `DB_URL`: Database connection URL (e.g., `jdbc:postgresql://localhost:5432/mydatabase`)
> - `DB_USER`: Database username
> - `DB_PASSWORD`: Database password
> - `DB_NAME`: Database name (e.g., `client_accounting`)

### 7. **Postman**
> - **Postman**: Version 11.21.0 or higher

## Installation

1. Clone rhe repository:
   ```sh
   https://github.com/LeonidGolysh/client-accounting.git
   ```
2. Configur your enviroment variables int the `application.properties` file and `.env`:
   ```properties
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   ```
   and
   ```properties
   DB_URL=jdbc:postgresql://localhost:5432/mydatabase
   DB_USERNAME=database username
   DB_PASSWORD=database password
   DB_NAME=database name
   ```
3. Before running the program, you need to run Docker:
   ```sh
   docker-compose start
   ```

## Use
