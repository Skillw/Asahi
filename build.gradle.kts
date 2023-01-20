plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.55"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
}

val order: String? by project

task("versionModify") {
    project.version = project.version.toString() + (order?.let { "-$it" } ?: "")
}

taboolib {
    description {
        contributors {
            name("Glom_")
        }
    }
    options("skip-kotlin-relocate")
    install("common")
    install("common-5")
    install("module-database")
    install("module-chat")
    install("platform-application")
    install("expansion-alkaid-redis")
    classifier = null
    version = "6.0.10-71"
    relocate("com.esotericsoftware.reflectasm", "com.skillw.reflectasm")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.esotericsoftware:reflectasm:1.11.9")
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}