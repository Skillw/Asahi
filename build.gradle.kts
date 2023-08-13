plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
}

tasks.javadoc {
    this.options {
        encoding = "UTF-8"
    }
}


val order: String? by project
val api: String? by project

task("versionModify") {
    project.version = project.version.toString() + (order?.let { "-$it" } ?: "")
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "com.skillw.asahi.Playground"
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
//jar {
//    manifest {
//        attributes 'Main-Class': 'MovieQuizBackendKt'
//    }
//}
taboolib {
    options("skip-kotlin-relocate", "keep-kotlin-module")
    description {
        contributors {
            name("Glom_")
        }
    }
    options("skip-kotlin-relocate")
    install("common")
    install("common-5")
    install("module-chat")
    install("module-kether")
    install("platform-application")
    classifier = null
    version = "6.0.11-31"
    relocate("com.esotericsoftware.reflectasm", "com.skillw.asahi.reflectasm")
    relocate("com.google.gson", "com.skillw.asahi.gson")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.esotericsoftware:reflectasm:1.11.9")
    compileOnly("com.google.code.gson:gson:2.9.0")
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {

    options.encoding = "UTF-8"
}


java {
    withJavadocJar()
    withSourcesJar()
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
            url = if (project.version.toString().contains("-SNAPSHOT")) {
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            } else {
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = project.findProperty("username").toString()
                password = project.findProperty("password").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
        mavenLocal()
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            version = project.version.toString()
            groupId = project.group.toString()
            pom {
                name.set(project.name)
                description.set("Dynamic Script Lang.")
                url.set("https://github.com/Glom-c/Asahi/")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/Glom-c/Asahi/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("Skillw")
                        name.set("Glom_")
                        email.set("glom@skillw.com")
                    }
                }
                scm {
                    connection.set("...")
                    developerConnection.set("...")
                    url.set("...")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications.getAt("library"))
}