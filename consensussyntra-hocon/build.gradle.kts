plugins {
    `java-library`
    alias(libs.plugins.defaults)
    alias(libs.plugins.metadata)
    alias(libs.plugins.javadocLinks)
    `maven-publish`
    signing
    alias(libs.plugins.mavenCentralPublishing)
    alias(libs.plugins.spotbugs)
    checkstyle
}

group = "io.consensussyntra"
version = "0.9-SNAPSHOT"

metadata {
    moduleName = "io.consensussyntra.hocon"
    readableName = "ConsensusSyntra HOCON Config Parser"
    description = "HOCON config parser for ConsensusSyntra"
    license {
        apache2()
    }
    organization {
        name = "ConsensusSyntra"
        url = "https://consensussyntra.io"
    }
    developers {
        register("metanet") {
            fullName = "Ensar Basri Kahveci"
            email = "ebkahveci@gmail.com"
        }
        register("mdogan") {
            fullName = "Mehmet Dogan"
            email = "mehmet@dogan.io"
        }
    }
    github {
        org = "ConsensusSyntra"
        pages()
        issues()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Jar>().configureEach {
    manifest.attributes(
        "Implementation-Title" to project.name,
        "Implementation-Vendor" to metadata.organization.provider.flatMap { it.name },
        "Implementation-Version" to provider { project.version.toString() },
    )
}

dependencies {
    api(project(":consensussyntra"))
    api(libs.typesafe.config)
    compileOnly(libs.findbugs.annotations)
}

@Suppress("UnstableApiUsage") //
testing {
    suites {
        withType<JvmTestSuite> {
            useJUnit(libs.versions.junit)
        }
        named<JvmTestSuite>("test") {
            dependencies {
                implementation(libs.assertj)
                implementation(testFixtures(project(":consensussyntra")))
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("main") {
            from(components["java"])
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["main"])
}
