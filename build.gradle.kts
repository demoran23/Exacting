val slayTheSpireInstallDir = "${System.getenv("STS_HOME")}"

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.31"))
    }
}

plugins {
    application
    kotlin("jvm") version "1.2.71"
}

application {
    mainClassName = "exacting.ExactBlocking"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    compileClasspath(fileTree("lib"))
    compileClasspath(files("$slayTheSpireInstallDir\\desktop-1.0.jar"))
}

sourceSets {
    main {
        java {
            srcDirs.add(file("src/main/kotlin/"))
        }
    }
}

tasks.register<Jar>("jar1") {
    archiveName = "Exacting.jar"
    from(sourceSets.main.get().output) { }
    dependsOn(configurations.runtimeClasspath)
    manifest {
        attributes["Main-Class"] = "exacting.ExactBlocking"
    }
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
    from(file("src/ModTheSpire.json"))
}

tasks.register<Copy>("copyJarToStsMods") {
    dependsOn("clean")
    dependsOn("jar1")

    if (slayTheSpireInstallDir == null || slayTheSpireInstallDir == "null") {
        throw Exception("STS_HOME is not set.")
    }

    from("build/libs/Exacting.jar")
    into("$slayTheSpireInstallDir\\mods")
}

tasks.register<Copy>("copyJarToWorkshopFolder") {
    dependsOn("clean")
    dependsOn("jar1")

    if (slayTheSpireInstallDir == null || slayTheSpireInstallDir == "null") {
        throw Exception("STS_HOME is not set.")
    }

    from("build/libs/Exacting.jar")
    into("$slayTheSpireInstallDir\\Exacting\\content") // publish to Workshop folder
}
