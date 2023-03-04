import com.google.protobuf.gradle.protobuf

plugins {
    java
    idea
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("com.google.protobuf") version "0.9.2"
    id("io.freefair.lombok") version "6.6.3"
}

group = "com.jds.neo4j.reactive"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val protobufVersion = "3.21.7"
val testcontainersVersion = "1.17.6"
val reactorVersion = "3.4.11"
val springfoxSwaggerVersion = "3.0.0"
val springdocOpenApiVersion = "1.6.14"
val javaxAnnotationVersion = "1.3.2"
val protobufGradlePluginVersion = "0.8.18"
val grpcVersion = "1.45.1"
val neo4jHarnessVersion = "3.5.12"
val junitJupiterVersion = "5.8.2"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.springfox:springfox-swagger2:$springfoxSwaggerVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenApiVersion")
    implementation("org.projectlombok:lombok")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("io.projectreactor:reactor-core:$reactorVersion") // Add this line if you're using Reactor
    implementation("javax.annotation:javax.annotation-api:$javaxAnnotationVersion")
    implementation("com.google.protobuf:protobuf-gradle-plugin:$protobufGradlePluginVersion")
    implementation("org.neo4j.test:neo4j-harness:$neo4jHarnessVersion")

    protobuf(files("proto/"))

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:neo4j:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

sourceSets {
    named("main") {
        proto {
            srcDir("src/main/proto")
        }
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Copy> {
    filesMatching("**/*.proto") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks.test {
    // Use the built-in JUnit support of Gradle.
    useJUnitPlatform()
}
