# Sistema de Asignación de Salas (Caso de Estudio en Kotlin)

Este proyecto es una demostración de programación funcional en Kotlin, enfocada en la inmutabilidad, las funciones puras y el estado sin efectos secundarios (referential transparency). Implementa la lógica de un **Sistema de Asignación de Salas** para gestionar solicitudes de reserva basándose en la capacidad, equipamiento y disponibilidad.

## Requisitos previos

- Tener **Java JDK 21** o superior instalado en el sistema. (Recomendado, aunque versiones más antiguas pueden funcionar dependiendo de la configuración de Gradle).

## Instrucciones de Ejecución

Para compilar y ejecutar el proyecto, ingresa a la raíz del proyecto usando la consola/terminal. Tienes dos formas de ejecutarlo:

**En Windows:**

```bat
gradlew.bat run
```

**En Linux y macOS:**

```bash
chmod +x gradlew
./gradlew run
```

## Ubicación de archivos

El código funcional que se encuentra en `src/main/kotlin/dev/patata/caso/Funcional.kt` mientras que su contraparte orientada a objetos se encuentra en `src/main/kotlin/dev/patata/caso/POO.kt` para posteriormente compararlas adecuadamente.

## Conceptos Clave Demostrados

El código funcional (que se encuentra en `src/main/kotlin/dev/patata/caso/Funcional.kt`) ilustra las siguientes prácticas:

1. **Modelos Inmutables**: Utiliza `data class` para definir entidades (`Sala`, `Solicitud`, `Asignacion`, `Rechazo`, `EstadoSistema`) y evitar la mutación de estado. Cuando cambia el sistema, se crean nuevas instancias usando el método `.copy()` (por ejemplo, en lugar de `lista.add(...)` se hace `asignaciones + nuevaAsignacion`).
2. **Funciones Puras**: Funciones de validación como `cumpleCapacidad`, `cumpleEquipamiento`, `estaOcupada` y `salaValidaPara` dependen enteramente de los parámetros de entrada y no tienen efectos secundarios.
3. **Procesamiento de Flujos Puros**: Uso de `fold` (`procesarFlujo`) en lugar de bucles `for`/`while` tradicionales, lo cual elimina variables de estado local (`var`) para el acumulador y pasa un estado constante hacia cada iteración de manera funcional.
