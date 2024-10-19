# Testing Instructions

To test the application, follow these steps:

## Prerequisites

1. **Docker**
2. **Docker Compose**


## Steps to Test

1. **Obtain a Valid Cookie**:
    - Visit [Dummy REST API Example](https://dummy.restapiexample.com/api/v1) using a web browser.
    - Inspect the network requests and copy a valid cookie from the request headers.

2. **Update `HttpHeaderUtil` Class**:
    - Open the `src/main/java/com/example/rqchallenge/util/HttpHeaderUtil.java` file.
    - Replace the `redacted-cookie` placeholder with the valid cookie you obtained.

    ```java
    private static final String COOKIE = "your-valid-cookie-here";
    ```

3. **Bring Up the Application Using Docker Compose**:
    - Open a terminal in the project root directory.
    - Use Docker Compose to bring up the application and the database:

    ```sh
    docker-compose up
    ```

4. **Make Requests to the Controller**:
    - Use a tool like Postman or `curl` to make requests to the application's controller endpoints.
    - Example `curl` command:

    ```sh
    curl -k http://localhost:8080/api/v1/employees
    ```

## Notes

- Database creds loaded as an env variable in `docker-compose.yml` 
- The application will call the external API and return the response based on the provided cookie.