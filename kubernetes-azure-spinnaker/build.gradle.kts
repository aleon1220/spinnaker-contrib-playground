plugins {
    application
    alias(libs.plugins.axion)
    alias(libs.plugins.shadow)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Source: https://mvnrepository.com/artifact/com.pulumi/pulumi
    implementation("com.pulumi:pulumi:1.26.0")

    // Source: https://mvnrepository.com/artifact/com.pulumi/azure-native
    implementation("com.pulumi:azure-native:3.17.0")
}

application {
    mainClass.set("myproject.App")
}

// Configure Axion to look for tags starting with 'v'
scmVersion {
    tag {
        prefix.set("v")
    }
}

// Crucial step: Set the Gradle project version to the version Axion discovered
version = scmVersion.version

tasks.shadowJar {
    isZip64 = true

    minimize() // todo: test if outcome is a smaller jar
}