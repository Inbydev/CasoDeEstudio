# Sistema de Asignación de Salas (Caso de Estudio en Kotlin)

Este proyecto es una demostración de programación funcional en Kotlin, enfocada en la inmutabilidad, las funciones puras y el estado sin efectos secundarios (referential transparency). Implementa la lógica de un **Sistema de Asignación de Salas** para gestionar solicitudes de reserva basándose en la capacidad, equipamiento y disponibilidad.

## Requisitos previos

- Tener **Java JDK 21** o superior instalado en el sistema.

> [!NOTE]
> Para Windows: [Tutorial YouTube](https://www.youtube.com/watch?v=dOCHNoy-lTE)

> [!NOTE]
> Para Linux: [java-21-para-linux](docs/java-21-para-linux.md)

### Verificación de Instalación

Una vez instalado, confirma la versión ejecutando:

```bash
java -version
```

- Tener [IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/download/) instalado si se compila desde este mismo IDE.

## Instrucciones de uso

Antes que nada, clona este repositorio desde GitHub con el botón verde que dice **Code** y clickee en descargar como ZIP.

Una vez descargado, extraer este archivo y continue con las siguientes instrucciones dependiendo del modo que guste ejecutar esta implementación básica del caso de estudio.

## Instrucciones de Ejecución desde IntelliJ IDEA

Si desea ejecutar esta implementación desde este IDE, primero debes abrir IntelliJ IDEA; si tiene algún proyecto abierto, darle a las cuatro barras que se ubica en la esquina superior izquierda y luego darle a cerrar proyecto.

Luego de esto, clickear en abrir y seleccionar carpeta donde extrajo el proyecto clonado y abra la carpeta `src/main/kotlin/dev/ia/caso` y abrir archivo `Funcional.kt`.

Luego de estar dentro de este archivo, esperar hasta que se sincronice el proyecto (en la barra izquierda hay un icono de martillo y al darle click se mostrará el proceso de sincronización). Una vez finalizado, podrá clickear el botón verde con icono de flecha verde que se encuentra en la esquina superior derecha para ejecutar el código con el paradigma Funcional.

## Instrucciones de Ejecución desde terminal

Si desea ejecutar esta implementación desde la terminal, ingresa a la raíz del proyecto usando la consola/terminal:

```bash
cd raiz/del/proyecto
```

y luego dependiendo de su sistema operativo, ejecute el siguiente comando para ejecutar la implementación del caso de estudio en el paradigma funcional:

**En Windows:**

```bat
.\gradlew.bat run
```

**En Linux y macOS:**

```bash
chmod +x gradlew
./gradlew run
```

## Ubicación de archivos

El código funcional que se encuentra en `src/main/kotlin/dev/ia/caso/poo/Funcional.kt` mientras que su contraparte orientada a objetos se encuentra en `src/main/kotlin/dev/ia/caso/POO.kt` para posteriormente compararlas adecuadamente.

## Conceptos Clave Demostrados

El código funcional (que se encuentra en `src/main/kotlin/dev/ia/caso/Funcional.kt`) ilustra las siguientes prácticas:

1. **Modelos Inmutables**: Utiliza `data class` para definir entidades (`Sala`, `Solicitud`, `Asignacion`, `Rechazo`, `EstadoSistema`) y evitar la mutación de estado. Cuando cambia el sistema, se crean nuevas instancias usando el método `.copy()` (por ejemplo, en lugar de `lista.add(...)` se hace `asignaciones + nuevaAsignacion`).
2. **Funciones Puras**: Funciones de validación como `cumpleCapacidad`, `cumpleEquipamiento`, `estaOcupada` y `salaValidaPara` dependen enteramente de los parámetros de entrada y no tienen efectos secundarios.
3. **Procesamiento de Flujos Puros**: Uso de `fold` (`procesarFlujo`) en lugar de bucles `for`/`while` tradicionales, lo cual elimina variables de estado local (`var`) para el acumulador y pasa un estado constante hacia cada iteración de manera funcional.
