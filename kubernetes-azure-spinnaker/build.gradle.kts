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
    implementation("com.pulumi:pulumi:1.3.+")
    implementation("com.pulumi:azure-native:3.5.+")
}

application {
    mainClass.set("myproject.App")
}
