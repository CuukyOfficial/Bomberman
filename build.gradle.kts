plugins {
    id("java")
    id("com.gradleup.shadow") version "9.6.1"
}

group = "de.varoplugin"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    mavenCentral()
    
    maven {
        name = "varoplugin"
        url = uri("https://repo.varoplugin.de/releases")
    }

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    shadow(libs.paper)

    implementation(libs.jaskl)
    implementation(libs.slams)
    implementation(libs.cfw)
    
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    enableAutoRelocation = true
    relocationPrefix = "de.varoplugin.bomberman.dependencies"
}
