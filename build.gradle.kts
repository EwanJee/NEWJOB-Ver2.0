plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.9"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "web"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // XML 데이터를 자바 객체로 매핑하는 데 사용
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    // hashing key
    implementation("commons-codec:commons-codec:1.16.0")
    // AWS S3
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // oauth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    // spring security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    // prometheus
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // dotenv
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
    implementation("software.amazon.awssdk:s3:2.31.6")
    implementation("io.awspring.cloud:spring-cloud-aws-starter:3.1.1")
    // 카카오톡 알림톡 API
    implementation("net.nurigo:sdk:4.3.2")
    // Kotlin JDSL
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.5.5")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.5.5")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.5.5")
    // pdf editor
    implementation("com.itextpdf:itext7-core:8.0.3")
//    implementation("com.itextpdf:itext7-layout:8.0.3")
    // jsonb
    implementation("com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.5")
    // csv reader
    implementation("org.apache.commons:commons-csv:1.8")
    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("io.netty:netty-all")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // 지금은 안쓰므로 주석처리
//    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.9")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.3")
//    implementation("io.projectreactor.kotlin:reactor-kolin-extensions")
    // flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // cache
    implementation("org.springframework.boot:spring-boot-starter-cache:3.3.9")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.session:spring-session-data-redis:3.3.6")
//    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
