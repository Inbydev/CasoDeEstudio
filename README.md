# Sistema de Asignación de Salas (Caso de Estudio en Kotlin)

Este proyecto es una demostración de programación funcional en Kotlin, enfocada en la inmutabilidad, las funciones puras y el estado sin efectos secundarios (referential transparency). Implementa la lógica de un **Sistema de Asignación de Salas** para gestionar solicitudes de reserva basándose en la capacidad, equipamiento y disponibilidad.

## Conceptos Clave Demostrados

El código (que se encuentra en `src/main/kotlin/dev/patata/caso/Main.kt`) ilustra las siguientes prácticas:

1. **Modelos Inmutables**: Utiliza `data class` para definir entidades (`Sala`, `Solicitud`, `Asignacion`, `Rechazo`, `EstadoSistema`) y evitar la mutación de estado. Cuando cambia el sistema, se crean nuevas instancias usando el método `.copy()` (por ejemplo, en lugar de `lista.add(...)` se hace `asignaciones + nuevaAsignacion`).
2. **Funciones Puras**: Funciones de validación como `cumpleCapacidad`, `cumpleEquipamiento`, `estaOcupada` y `salaValidaPara` dependen enteramente de los parámetros de entrada y no tienen efectos secundarios.
3. **Procesamiento de Flujos Puros**: Uso de `fold` (`procesarFlujo`) en lugar de bucles `for`/`while` tradicionales, lo cual elimina variables de estado local (`var`) para el acumulador y pasa un estado constante hacia cada iteración de manera funcional.

## Requisitos previos

- Tener **Java JDK 21** o superior instalado en el sistema. (Recomendado, aunque versiones más antiguas pueden funcionar dependiendo de la configuración de Gradle).

## Instrucciones de Ejecución

Para compilar y ejecutar el proyecto, ingresa a la raíz del proyecto usando la consola/terminal. Tienes dos formas de ejecutarlo:

### Opción 1: Usando el Gradle Wrapper (Recomendado)

El Wrapper de Gradle (`gradlew`) descargará la versión correcta de Gradle automáticamente si es la primera vez que se ejecuta. No necesitas tener Gradle instalado en tu sistema.

**En Windows:**
```bat
gradlew.bat run
```

**En Linux y macOS:**
```bash
chmod +x gradlew
./gradlew run
```

### Opción 2: Sin usar el Wrapper (Requiere Gradle instalado)

Si decides borrar los archivos del wrapper (`gradlew`, `gradlew.bat` y la carpeta `gradle/`) o prefieres usar tu propia instalación de Gradle, necesitas tener [Gradle instalado globalmente](https://gradle.org/install/) en tu sistema.

Ejecuta el siguiente comando en cualquier sistema operativo:

```bash
gradle run
```

---

Al ejecutar el programa (de cualquiera de las dos formas), deberías observar la salida estándar con el "INFORME DE ASIGNACIÓN DE SALAS" indicando qué solicitudes fueron aceptadas, en qué salas se asignaron, y el motivo detallado de las solicitudes rechazadas.
