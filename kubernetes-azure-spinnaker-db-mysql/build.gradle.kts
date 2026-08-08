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
    implementation(libs.pulumiJava)
    implementation(libs.azureNative)
}

application {
    mainClass.set("myproject.App")
}