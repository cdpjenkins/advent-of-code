plugins {
    kotlin("jvm") version "1.5.10"
}

group = "com.cdpjenkins"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val kotestVersion = "5.0.3"

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")


    testImplementation("com.natpryce:hamkrest:1.8.0.1")
}
