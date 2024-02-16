plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    `java-library`
    `maven-publish`
    signing
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.1.1"
}

group = "io.github.magonxesp"
version = "1.0.0"

publishing {
    publications {
        register<MavenPublication>("zerochan-client") {
            artifactId = "zerochan-client"
            from(components["java"])
        }
    }
}

signing {
    sign(publishing.publications["zerochan-client"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

centralPortal {
    pom {
        name = "Zerochan Client"
        description = "Zerochan API client for kotlin"
        url = "https://github.com/magonxesp/zerochan-client"
        licenses {
            license {
                name = "The MIT License (MIT)"
                url = "https://mit-license.org/"
            }
        }
        developers {
            developer {
                id = "magonxesp"
                name = "MagonxESP"
                email = "magonxesp@gmail.com"
                url = "https://github.com/magonxesp"
            }
        }
        scm {
            connection = "scm:git:git://github.com/magonxesp/zerochan-client.git"
            developerConnection = "scm:git:ssh://github.com/magonxesp/zerochan-client.git"
            url = "https://github.com/magonxesp/zerochan-client"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val ktor_version: String by project
    val kotlin_serialization_version: String by project
    val kotlin_corroutines_version: String by project
    val kotest_version: String by project

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_serialization_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_corroutines_version")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}