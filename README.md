# Moodle Project

The Moodle Project is a sophisticated, Java-based educational management platform designed for scalability.<br>
Leveraging Spring Boot, Java 17, and Maven, it integrates with AWS services for robust functionality.<br> 
The project includes detailed instructions for build processes, utilizes Docker for AWS services like S3 and CloudWatch, 
and features comprehensive API documentation through SpringDoc.<br> 
It's tailored for efficient content sharing, interaction, and progress tracking, making it a valuable tool for educational environments.

## Table of Contents

- Build
- How to Run
- The available endpoints (Swagger)
- AWS services with Docker
- Auth credentials

## Build

- Java JDK version 17+ should be installed in the system

Check it here https://www.oracle.com/java/technologies/downloads/#java17
or here https://adoptium.net/temurin/releases/


To build the application execute the following commands in the project folder (where pom.xml and mvnw are located).<br>
Before building, set permissions on the ./mvnw file in the cloned project directory:
```bash
chmod 755 ./mvnw
```

```bash
./mvnw clean package # this will build the project
```
For the first time, it will download and install the Maven version configured in the project settings.<br>
Next time the cached version will be used without re-downloading.

After the build is completed, the folder `/target` will be created with a compiled `.jar` ready to be launched.


**To run code quality plugins separately**

```bash
# Checkstyle
./mvnw checkstyle:check

# PMD
./mvnw pmd:check
```

## How to Run:
Now you can launch the server for example at port `8080`
(if the option `--server.port=8080` is not provided the default port is `8080`):
```bash
java -jar ./target/*.jar --server.port=8080
```
It may take up to around 15 sec for the server to start

## The available endpoints (Swagger)

API documentation is generated in HTML, JSON and YAML formats
using [Spring Doc](https://springdoc.org/index.html) library.

*[Spring Doc FAQ](https://springdoc.org/faq.html#faq)*

Some basic information about the available endpoints is provided by Swagger at: http://localhost:8080/swagger-ui/index.html

## Run AWS services with Docker
Please download docker to run these commands.

You can download docker here for Windows.
- Windows https://www.docker.com/products/docker-desktop/

You can read manual and install on other systems here.
- Linux manual https://docs.docker.com/desktop/install/linux-install/
- Mac manual https://docs.docker.com/desktop/install/mac-install/

1) Run these commands in cmd to run AWS services(for now we have S3 bucket and CloudWatch) in Docker.

   `cd docker`

   `docker compose up`

---
When entering each link, the HTTP authentication method is called. The username and password for
authentication are:

Basic auth: `developer` `123456`

Admin user: `admin` `123456`