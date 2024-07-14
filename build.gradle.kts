import io.izzel.taboolib.gradle.*

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.izzel.taboolib") version "2.0.11"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.20"
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

taboolib {

    description {
        contributors {
            name("Glom_")
        }
        dependencies {}
    }

    env {
        // basic
        install(UNIVERSAL)

        install(KETHER)
    }


    version {
        if(project.gradle.startParameter.taskNames.getOrNull(0) == "taboolibBuildApi" || api != null){
            println("api!")
            isSkipKotlinRelocate =true
            isSkipKotlin = true
        }
        taboolib = "6.1.1-beta17"
    }

    relocate("org.openjdk.nashorn", "com.skillw.nashorn")
    relocate("jdk.nashorn", "com.skillw.nashorn.legacy")
    relocate("com.esotericsoftware.reflectasm", "com.skillw.asahi.reflectasm")
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