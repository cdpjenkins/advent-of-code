plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    testImplementation("com.natpryce:hamkrest:1.8.0.1")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/main/kotlin")
        }
        test {
            java.srcDirs("src/test/kotlin")
        }
    }

    wrapper {
        gradleVersion = "7.3"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
