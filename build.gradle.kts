@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.ben-manes.versions") version "0.51.0"
    kotlin("multiplatform") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    application
}

group = "org.jd"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            webpackTask {
                mode = KotlinWebpackConfig.Mode.PRODUCTION
                sourceMaps = false
            }
            runTask {
                devServer = devServer?.copy(
                    port = 8081,
                    static = mutableListOf(
                        "$buildDir/js/packages/benoggl/kotlin/static",
                        "$buildDir/js/packages/benoggl/kotlin"
                    )
                )
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("com.benasher44:uuid:0.8.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
                implementation("org.springframework.boot:spring-boot-starter-rsocket")
                implementation("org.springframework.boot:spring-boot-starter-validation")
                implementation("org.springframework.boot:spring-boot-starter-web")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.11.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
                runtimeOnly("com.h2database:h2")
                runtimeOnly("io.r2dbc:r2dbc-h2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.736"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
                implementation("io.rsocket.kotlin:rsocket-core:0.15.4")
                implementation("io.rsocket.kotlin:rsocket-ktor-client:0.15.4")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("org.jd.benoggl.BenogglApplicationKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
