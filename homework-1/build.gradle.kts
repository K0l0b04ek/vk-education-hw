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
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("io.rest-assured:json-schema-validator:5.5.0")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform {
        // run by tag: ./gradlew test -Dgroups=smoke
        System.getProperty("groups")?.let { includeTags(it) }
    }
    systemProperty("baseUrl", System.getProperty("baseUrl", "https://jsonplaceholder.typicode.com"))
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = false
    }
}
