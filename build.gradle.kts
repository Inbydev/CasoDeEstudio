plugins {
    kotlin("jvm") version "2.4.10"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.patata.caso.FuncionalKt")
}

tasks.named<JavaExec>("run") {
    description = "Ejecuta la aplicación por defecto"
    standardInput = System.`in`
}

// Tarea específica para la versión Funcional
tasks.register<JavaExec>("runFun") {
    group = "application"
    description = "Ejecuta la versión Funcional"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("dev.patata.caso.FuncionalKt")
    standardInput = System.`in`
}

// Tarea específica para la versión POO
tasks.register<JavaExec>("runPoo") {
    group = "application"
    description = "Ejecuta la versión Orientada a Objetos (POO)"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("dev.patata.caso.poo.POOKt")
    standardInput = System.`in`
}
