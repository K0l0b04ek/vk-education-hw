plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

dependencies {
    testImplementation("com.codeborne:selenide:7.5.1")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.13")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform {
        // run by tags: ./gradlew test -DincludeTags=smoke  /  -DexcludeTags=negative
        System.getProperty("includeTags")?.let { includeTags(*it.split(",").toTypedArray()) }
        System.getProperty("excludeTags")?.let { excludeTags(*it.split(",").toTypedArray()) }
    }

    // forward -Dselenide.* options (e.g. -Dselenide.headless=false) to the test JVM
    System.getProperties().stringPropertyNames()
        .filter { it.startsWith("selenide.") }
        .forEach { systemProperty(it, System.getProperty(it)) }

    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = false
    }
}

// ./gradlew smokeTest - only tests tagged "smoke"
tasks.register<Test>("smokeTest") {
    group = "verification"
    description = "Runs only tests tagged 'smoke'"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform { includeTags("smoke") }
}
