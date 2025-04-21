# CashCard API

This is a Spring Boot application designed to manage cash cards with basic functionality such as creating, retrieving, updating, and deleting cards.

## Prerequisites

- Java JDK 17 or later
- Maven 3.6 or later (for building the project)

## Setup

To get started with this project:

1. **Clone the repository**:
   ```sh
   git clone https://github.com/0xsuid/cashcard-api.git
   cd cashcard-api
   ```

2. **Build the project**:
   ```sh
   ./gradlew build
   ```

3. **Run the application**:
   ```sh
   ./gradlew bootRun
   ```

## Endpoints

- **GET /cashcards**: Retrieve all cash cards.
- **POST /cashcards**: Create a new cash card.
- **GET /cashcards/{id}**: Retrieve a specific cash card by ID.
- **PUT /cashcards/{id}**: Update an existing cash card.
- **DELETE /cashcards/{id}**: Delete a specific cash card.

## Testing

The project includes comprehensive tests for the endpoints. To run the tests:

```sh
./gradlew test
```

### Sample Test Case

Here is a sample test case that retrieves a cash card by ID:

```java
@Test
void shouldReturnACashCardWhenDataIsSaved() {
    ResponseEntity<String> response = restTemplate
            .withBasicAuth("sarah1", "abc123")
            .getForEntity("/cashcards/99", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext documentContext = JsonPath.parse(response.getBody());
    Number id = documentContext.read("$.id");
    assertThat(id).isEqualTo(99);

    Double amount = documentContext.read("$.amount");
    assertThat(amount).isEqualTo(123.45);
}
```

This test ensures that the API correctly retrieves a cash card with the specified ID and verifies its contents.

## Security

The application includes basic authentication for managing cash cards. Ensure to set up appropriate credentials in your environment.

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests or open issues for bug reports or feature suggestions.