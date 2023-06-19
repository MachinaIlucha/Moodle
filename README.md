# Moodle Project

## Table of Contents

---
- Build
- Swagger
    - Dependency Management
    - Navigation

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
When entering each link, the HTTP authentication method is called. The username and password for
authentication are:

Basic auth: `developer` `123456`

Admin user: `admin` `123456`