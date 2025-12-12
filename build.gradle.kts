plugins {
    kotlin("jvm") version "2.2.20"
}

group = "com.fortumanov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("tools.aqua:z3-turnkey:4.14.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}