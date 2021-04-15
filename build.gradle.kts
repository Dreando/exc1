import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
}

group = "org.dreando"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.rest-assured:rest-assured:4.3.3")
    // For the purpose of rest-assured 4.3 due to some version clashes with spring
    testImplementation("io.rest-assured:rest-assured-common:4.3.3")
    testImplementation("io.rest-assured:json-path:4.3.3")
    testImplementation("io.rest-assured:xml-path:4.3.3")
    // Using 2.2.0 instead of available 3.0.0 because it is not compatible with spring yet (springs fault)
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.2.0")
}

// For the purpose of rest-assured 4.3
extra.apply { set("groovy.version", "3.0.3") }

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
