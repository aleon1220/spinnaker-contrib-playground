plugins {
    application
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
    implementation(libs.pulumiJava)
    implementation(libs.azureNative)
}

application {
    mainClass.set("myproject.App")
}

tasks.shadowJar {
    isZip64 = true
    minimize()
}
