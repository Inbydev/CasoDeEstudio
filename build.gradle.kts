plugins {
    kotlin("jvm") version "2.4.10"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.patata.caso.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
