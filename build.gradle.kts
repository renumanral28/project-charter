plugins {
    id("org.springframework.boot") version "2.4.5"  // Use the desired version of Spring Boot
    id("io.spring.dependency-management") version "1.0.11.RELEASE"  // Optional, for easier dependency management
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") // Starter for building web applications
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // For JPA and Hibernate
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf") // Optional for Thymeleaf template engine
   // implementation("org.springframework.boot:spring-boot-starter-security") // Optional for security features
    implementation("org.springframework.boot:spring-boot-starter-actuator") // Optional for monitoring
    implementation("com.h2database:h2") // In-memory database (optional, for testing)
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Spring Boot testing support
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.mockito:mockito-core:4.0.0") // Replace with the version you need

}

tasks.test {
    useJUnitPlatform()
}