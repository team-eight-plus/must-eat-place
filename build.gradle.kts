import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.2.0"
  id("io.spring.dependency-management") version "1.1.4"
  id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
  kotlin("jvm") version "1.9.20"
  kotlin("plugin.spring") version "1.9.20"
  kotlin("plugin.jpa") version "1.9.20"
  kotlin("kapt") version "1.9.20"
  id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
}

group = "com.go"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-actuator")

  implementation("mysql:mysql-connector-java:8.0.31")

  val querydslVersion = "5.0.0" //querydsl
  implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
  kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
  kapt("com.querydsl:querydsl-kotlin-codegen:5.0.0")

  kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
  annotationProcessor("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")

  implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
  implementation("io.github.resilience4j:resilience4j-reactor:1.7.0")
  implementation("io.github.resilience4j:resilience4j-all:1.7.1")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  testImplementation("io.mockk:mockk:1.12.0")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}
