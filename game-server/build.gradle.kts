plugins {
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.allopen") version "1.5.0"
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-container-image-docker:1.13.3.Final")
    implementation("io.quarkus:quarkus-rest-client:1.13.3.Final")
    implementation("io.quarkus:quarkus-resteasy-jackson:1.13.3.Final")
    implementation("io.quarkus:quarkus-kotlin:1.13.3.Final")
    implementation("io.quarkus:quarkus-resteasy:1.13.3.Final")
    implementation("io.quarkus:quarkus-jdbc-h2:1.13.3.Final")
    implementation("io.quarkus:quarkus-test-h2:1.13.3.Final")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin:1.13.3.Final")
    implementation(("io.quarkus:quarkus-hibernate-validator:1.13.3.Final"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0")
    implementation("io.quarkus:quarkus-arc:1.13.3.Final")
    implementation("io.quarkus:quarkus-smallrye-openapi:1.13.3.Final")
    implementation(project(":game-common"))
    testImplementation("io.quarkus:quarkus-junit5:1.13.3.Final")
    testImplementation("io.kotest:kotest-runner-junit5:4.5.0")
    testImplementation("io.rest-assured:rest-assured:4.3.3")
}

group = "org.jd.benoggl"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    kotlinOptions.javaParameters = true
}
