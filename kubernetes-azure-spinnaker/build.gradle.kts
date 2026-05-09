plugins {
    application
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
