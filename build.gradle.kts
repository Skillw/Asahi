import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}


group = "com.skillw.asahi"
version = "0.0.1-alpha"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.apache.commons:commons-lang3:3.5")
    compileOnly("com.google.guava:guava:21.0")
    testImplementation(kotlin("test"))

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    this.mainClass.set("com.skillw.asahi.MainKt")
    mainClassName = "com.skillw.asahi.MainKt"
}

tasks.withType<ShadowJar> {
    this.archiveClassifier.set("")
    archiveBaseName.set("${archiveBaseName.get()}-shaded")
    dependencies {
        include(dependency("org.apache.commons:commons-lang3:3.5"))
        include(dependency("com.google.guava:guava:21.0"))
    }
}