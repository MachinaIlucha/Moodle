# Moodle Project

## Table of Contents

---
- Build
- Swagger
    - Dependency Management
- AWS services with Docker
- Auth credentials

## Build

---
```bash
cd path/to/moodle/

./mvnw clean install
./mvnw compile
```

**To run code quality plugins separately**

```bash
# Checkstyle
./mvnw checkstyle:check

# PMD
./mvnw pmd:check
```

## Swagger

---

API documentation is generated in HTML, JSON and YAML formats
using [Spring Doc](https://springdoc.org/index.html) library.

*[Spring Doc FAQ](https://springdoc.org/faq.html#faq)*

### Dependency Management

---

>[springdoc-openapi-starter-webmvc-ui-2.0.2](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui/2.0.2)
*- SpringDoc OpenAPI WebMVC UI starter.*

---


## Run AWS services with Docker
Please download docker to run these commands.

You can download docker here for Windows.
- Windows https://www.docker.com/products/docker-desktop/

You can read manual and install on other systems here.
- Linux manual https://docs.docker.com/desktop/install/linux-install/
- Mac manual https://docs.docker.com/desktop/install/mac-install/

1) Run these commands in cmd to run AWS services in Docker.

   `cd docker`

   `docker compose up`

---
When entering each link, the HTTP authentication method is called. The username and password for
authentication are:

Basic auth: `developer` `123456`

Admin user: `admin` `123456`