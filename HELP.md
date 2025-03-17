# Read Me First

The following was discovered as part of building this project:

* The JVM level was changed from '23' to '21' as the Kotlin version does not support Java 23 yet.

# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.9/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.9/gradle-plugin/packaging-oci-image.html)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/6.1.17/languages/kotlin/coroutines.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.3.9/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/3.3.9/reference/data/nosql.html#data.nosql.mongodb)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/3.3.9/reference/data/nosql.html#data.nosql.redis)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.3.9/reference/using/devtools.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.3.9/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.3.9/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Validation](https://docs.spring.io/spring-boot/3.3.9/reference/io/validation.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.9/reference/web/servlet.html)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/3.3.9/reference/web/reactive.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* mongodb: [`mongo:latest`](https://hub.docker.com/_/mongo)
* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)
* redis: [`redis:latest`](https://hub.docker.com/_/redis)

Please review the tags of the used images and set them to the same as you're running in production.

