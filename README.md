# PoC Project with Neo4j

This project is a proof of concept to demonstrate how to use protobuff, Swagger APIs, Testcontainers and Neo4j to build
an
initial Neo4J playground.

The project consists of five services:

- **Currency Service:** This service is responsible for managing currency information, such as currency code, name,
  symbol, etc.
- **Exchange Service:** This service handles exchange rate calculation and conversion for currencies.
- **Price Service:** This service is responsible for managing price information, such as current market price,
  historical prices, etc.
- **Ticker Service:** This service handles real-time price updates for various assets.
- **Trade Service:** This service is responsible for managing trades made by users.

Each service is implemented using Java 17 and Spring Boot, and uses Swagger for API documentation.

## Prerequisites

You can use [chcolatey](https://community.chocolatey.org/) to install the following if you are using windows:

- Java 17 (`choco install openjdk`)
- Docker (`choco install docker-desktop`)
- Gradle (`choco install protoc`)
- Protoc (`choco install gradle`)

Optional:

- Neo4j Desktop ([Download](https://neo4j.com/download/))
- Rancher Desktop ([Download](https://rancherdesktop.io/))

I usually work with WSL2/Ubuntu but in this case I used plain Windows.

## Getting Started

To get started with this project, follow these steps:

1. Clone the repository:

```shell
git clone https://github.com/danielsobrado/neo4j-sandbox.git
```

2. Build the project:

```shell
cd poc-project
./gradlew clean build
```

3. Start the services with Docker Compose:

```shell
docker-compose up -d
```

4. Wait for the services to start (this may take a few minutes):

```shell
docker-compose logs -f
```

5. Stop the services:

```shell
docker-compose down
```

## Architecture

This project is built using a simple services and controllers, where each service is responsible for a specific business
domain. Communication between services is done using RESTful APIs over HTTP.

Each service is implemented using Spring Boot and uses Swagger for API documentation.

The data for this project is stored in a Neo4j graph database.

## Testing

This project uses Testcontainers to run integration tests. These tests spin up a temporary Neo4j database and run tests
against the services.

To run the tests, simply execute the following command:

```shell
docker-compose down
```

## Installing and Compiling Protobuf

### Benefits of using Probuf

Here are some of the benefits of using protobuf:

1. **Efficient serialization**: Protobuf employs a binary format that is both compact and efficient, allowing for faster
   data encoding and decoding. As a result, it is well-suited for use in high-performance, distributed systems.

2. **Language agnostic**: Because Protobuf is platform and language agnostic, you can use it to serialize data in one
   language and deserialize it in another without any compatibility issues.

3. **Backward and forward compatibility**: Because Protobuf supports backward and forward compatibility, you can add,
   remove, or modify message schema fields without breaking backward compatibility with existing clients.

4. **Generated code**: Code generation tools are included with Protobuf, allowing you to automatically generate
   language-specific code for
   encoding and decoding messages, reducing the amount of boilerplate code you must write.

5. **Schema validation**: Protobuf messages have a well-defined schema that can be used to validate data at runtime.
   This can aid in the
   detection of errors early in the development process, lowering the likelihood of bugs in production.

Overall, Protobuf is a strong and adaptable serialization format that can help you improve the performance and
reliability of your distributed systems.

**Note**: Protobuf doesn't make much sense at this stage, but it will once we move to a distributed architecture.

### To use Protobuf in this project

You need to install the Protobuf compiler on your system and compile the `.proto` files
into Java classes. Follow the steps below to install and compile Protobuf:

1. Install the Protobuf Compiler
   To install the Protobuf compiler, follow the instructions for your operating system on
   the [Protobuf Releases](https://github.com/protocolbuffers/protobuf/releases) page.
   Alternatively, you can use a package manager such as `brew` (on macOS) or `apt` (on Ubuntu) to install Protobuf.

2. Define Your `.proto` Files
   Define your `.proto` files according to the Protobuf Language Guide.

3. Compile the .proto Files
   To compile the `.proto` files, run the following command in your terminal:

```shell
$ protoc --java_out=<output_dir> <proto_file>
```

* Replace `<output_dir>` with the directory where you want the compiled Java files to be generated.
* Replace `<proto_file>` with the path to your `.proto` file.
  For example, to compile a `currency.proto` file in the `src/main/proto` directory and generate the Java files in the
  `src/main/java` directory, you would run:

```shell
$ protoc --java_out=src/main/java src/main/proto/currency.proto
```

4. Use the Compiled Java Classes
   After compiling the `.proto` files, you can use the generated Java classes in your project. To use the classes, make
   sure to add the compiled Java files to your classpath.

That's it! Now you know how to install and compile Protobuf for use in this project.

## API Documentation

Swagger is used to generate API documentation for each service. Once the services are running, you can view the Swagger
UI by navigating to the following URLs in your web browser:

- Currency Service: http://localhost:8080/swagger-ui/index.html#/currency-controller
- Exchange Service: http://localhost:8080/swagger-ui/index.html#/exchange-controller
- Price Service: http://localhost:8080/swagger-ui/index.html#/price-controller
- Ticker Service: http://localhost:8080/swagger-ui/index.html#/ticker-controller
- Trade Service: http://localhost:8080/swagger-ui/index.html#/trade-controller

![SwaggerExample](https://github.com/danielsobrado/neo4j-poc/blob/main/img/SwaggerExample.png "SwaggerExample")

You can find examples of json payloads in the test cases.

Or you can always do something like this:

```java
        Ticker ticker=Ticker.newBuilder()
        .setSymbol("ABC")
        .setName("ABC Inc.")
        .setExchange(Exchange.newBuilder()
        .setCode("NYSE")
        .setName("New York Stock Exchange")
        .setCountry("USA")
        .build())
        .setTimestamp(123456789L)
        .build();
        String tickerJson=JsonFormat.printer().print(ticker);
```

Visit your local Neo4J on: http://localhost:7474/browser/ (neo4j/neo4j1234 as per `application.properties`)

Pull all nodes from the database:

```cypher
MATCH (n) RETURN n
```

## Contributing

If you'd like to contribute to this project, please feel free to submit a pull request.
