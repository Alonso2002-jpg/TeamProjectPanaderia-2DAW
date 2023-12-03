plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "org.develop"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//Dependencia JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//Dependencia de Spring Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	//Dependencia de Validacion de datos
	implementation("org.springframework.boot:spring-boot-starter-validation")
	//Dependencia para Cache de Spring
	implementation("org.springframework.boot:spring-boot-starter-cache:2.4.0")
	//Dependencia de Mongo JPA
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	//Dependencia de Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")
	//Nogociacion de Contenido
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
	//Websockets
    implementation("org.springframework.boot:spring-boot-starter-websocket")
	//Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	// Para manejar los JWT tokens
    // JWT (Json Web Token)
    implementation("com.auth0:java-jwt:4.4.0")
	//Gson
	implementation("com.google.code.gson:gson")
	//H2
	implementation("com.h2database:h2")
	    // PostgreSQL - Para producción
    implementation("org.postgresql:postgresql")
	//Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	// Test seguridad
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
