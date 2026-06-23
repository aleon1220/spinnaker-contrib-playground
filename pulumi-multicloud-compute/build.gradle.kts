plugins {
    application
    id("java")
}

group = "myproject"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.pulumi:pulumi:1.0.0")
    implementation("com.pulumi:aws:6.66.0")
    implementation("com.pulumi:azure-native:2.82.0")
    implementation("com.pulumi:gcp:8.12.0")
}

application {
    mainClass.set("myproject.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
