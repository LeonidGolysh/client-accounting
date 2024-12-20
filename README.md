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
Once Docker is running, you can run the program. 

First, you need to fill in the table with possible services and price. The application provides a REST API for creating, reading, updating, and deleting data.

Create:
* **URL:** /api/clients-accounting/price-service/create
* **Method:** POST
* **Example of a querty:**
> http://localhost:8080/api/clients-accounting/price-service/create
```
{
    "serviceName": "Car wash",
    "price": "500"
}
```

Get all and get by id:
* **URL:** /api/clients-accounting/price-service
* **URL:** /api/clients-accounting/price-service/{id}
* **Method:** POST
* **Path options:**
   * `id` -  service id
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting/price-service
>
>http://localhost:8080/api/clients-accounting/price-service/{id}

Update service:
* **URL:** /api/clients-accounting/price-service/edit/{id}
* **Method:** PUT
* **Path options:**
   * `id` -  service id
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting/price-service/edit/{id}
```
{
    "serviceName": "Car wash",
    "price": "600"
}
```

Delete service:
* **URL:** /api/clients-accounting/price-service/delete/{id}
* **Method:** DELETE
* **Path options:**
   * `id` -  service id
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting/price-service/delete/{id}

### Answer
A successful response for the create method will contain a JSON array with the id of the created service.
```
{
   "id": 4
}
```
A successful response for getting a list of all services or one by id will be a JSON array of all or one service.
```
{
   "id": 1,
   "serviceName": "service name",
   "price": 100
}
```
A successful response after updating the information in the table will be a JSON array with updated data.
```
{
   "id": 1,
   "serviceName": "service name",
   "price": 200
}
```

### Filling in the table with clients
After you have filled in the table with services, you can fill in the table in which the accounting of clients will take place.

To create a client and other data, fill in the JSON array written below. Services are recorded via service IDs:
* **URL:** /api/clients-accounting/create
* **Method:** POST
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting/create
```
{
    "clientName": "Client Name",
    "phoneNumber": "Phone Number",
    "carModel": "Car Model",
    "carColor": "Car Color",
    "carNumberPlate": "Number Plate",
    "services": [
        "1",
        "2"
    ],
    "orderDate": "2024-07-08T15:30"
}
```

Implemented search for clients by order ID or by car model, by default shows the entire list of clients:
* **URL:** /api/clients-accounting
* **URL:** /api/clients-accounting/{idOrder}
* **URL:** /api/clients-accounting/{carModel}
* **Method:** GET
* **Path options:**
   * `idOrder` - order number under which the client is registered
   * `carModel` - when using search by car model it will display a list of all identical models
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting
>
>http://localhost:8080/api/clients-accounting/{idOrder}
>
>http://localhost:8080/api/clients-accounting/{carModel}

To change the data, specify the order ID and fill in the JSON array similar to that for creation:
* **URL:** /api/clients-accounting/edit/{idOrder}
* **Method:** PUT
* **Path options:**
   * `idOrder` - order number under which the client is registered
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting/edit{idOrder}
```
{
    "clientName": "Client Name",
    "phoneNumber": "Phone Number",
    "carModel": "Car Model",
    "carColor": "Car Color",
    "carNumberPlate": "Number Plate",
    "services": [
        "1",
        "2"
    ],
    "orderDate": "2024-07-08T15:30"
}
```

To delete an order, use the order ID:
* **URL:** /api/clients-accounting/delete/{idOrder}
* **Method:** DELETE
* **Path options:**
   * `idOrder` - order number under which the client is registered
* **Example of a querty:**
>http://localhost:8080/api/clients-accounting/delete/{idOrder}

