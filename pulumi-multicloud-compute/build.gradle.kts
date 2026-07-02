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
    implementation(libs.pulumiJava)
    implementation(libs.awsProvider)
    implementation(libs.azureNative)
    implementation(libs.gcpProvider)
}

application {
    mainClass.set("myproject.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
