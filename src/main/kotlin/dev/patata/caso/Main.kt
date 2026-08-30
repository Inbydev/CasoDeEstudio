package dev.patata.caso

//Asistido por IA

// Modelos Inmutables: Sala, Solicitud, Asignacion, Rechazo y EstadoSistema nunca se mutan.
// Funciones puras: cumpleCapacidad, cumpleEquipamiento, estaOcupada, salaValidaPara todas reciben datos y devuelven un resultado, sin efectos secundarios.


data class Franja(val inicio: Int, val fin: Int) {
    init {
        require(inicio < fin) { "La franja debe tener inicio < fin" }
    }

    fun seSolapaCon(otra: Franja): Boolean =
        inicio < otra.fin && otra.inicio < fin

    override fun toString(): String = "%02d:%02d-%02d:%02d".format(
        inicio / 60, inicio % 60, fin / 60, fin % 60
    )
}

data class Sala(
    val nombre: String,
    val capacidad: Int,
    val equipamiento: Set<String>
)

data class Solicitud(
    val id: String,
    val franja: Franja,
    val asistentes: Int,
    val equipoRequerido: Set<String>
)

data class Asignacion(val solicitud: Solicitud, val sala: Sala)

data class Rechazo(val solicitud: Solicitud, val motivo: String)


data class EstadoSistema(
    val asignaciones: List<Asignacion> = emptyList(),
    val rechazos: List<Rechazo> = emptyList()
)

// ---------------------------------------------------------------------
// Reglas de negocio — funciones puras
// ---------------------------------------------------------------------

fun cumpleCapacidad(sala: Sala, solicitud: Solicitud): Boolean =
    sala.capacidad >= solicitud.asistentes

fun cumpleEquipamiento(sala: Sala, solicitud: Solicitud): Boolean =
    sala.equipamiento.containsAll(solicitud.equipoRequerido)

fun estaOcupada(sala: Sala, franja: Franja, asignaciones: List<Asignacion>): Boolean =
    asignaciones.any { it.sala == sala && it.solicitud.franja.seSolapaCon(franja) }

//Concepto diferencial 2: salaValidaPara recibiendo asignaciones como parámetro no como estado interno
//(transparencia diferencial)
fun salaValidaPara(sala: Sala, solicitud: Solicitud, asignaciones: List<Asignacion>): Boolean =
    cumpleCapacidad(sala, solicitud) &&
            cumpleEquipamiento(sala, solicitud) &&
            !estaOcupada(sala, solicitud.franja, asignaciones)

fun buscarSalaDisponible(
    catalogo: List<Sala>,
    solicitud: Solicitud,
    asignaciones: List<Asignacion>
): Sala? = catalogo.firstOrNull { sala -> salaValidaPara(sala, solicitud, asignaciones) }


fun determinarMotivoRechazo(
    catalogo: List<Sala>,
    solicitud: Solicitud,
    asignaciones: List<Asignacion>
): String {
    val ningunaTieneCapacidad = catalogo.none { cumpleCapacidad(it, solicitud) }
    val ningunaTieneEquipo = catalogo.none { cumpleEquipamiento(it, solicitud) }
    val factibles = catalogo.filter { cumpleCapacidad(it, solicitud) && cumpleEquipamiento(it, solicitud) }

    return when {
        ningunaTieneCapacidad ->
            "Ninguna sala tiene capacidad suficiente para ${solicitud.asistentes} asistentes"

        ningunaTieneEquipo ->
            "Ninguna sala cuenta con el equipamiento requerido: ${solicitud.equipoRequerido.joinToString()}"

        factibles.isEmpty() ->
            "Ninguna sala cumple simultáneamente la capacidad y el equipamiento requeridos"

        factibles.all { estaOcupada(it, solicitud.franja, asignaciones) } ->
            "Las salas que cumplen los requisitos (${factibles.joinToString { it.nombre }}) " +
                    "están ocupadas en la franja ${solicitud.franja}"

        else ->
            "No fue posible asignar una sala por restricciones combinadas"
    }
}

// ---------------------------------------------------------------------
// Procesamiento del flujo — núcleo funcional
// ---------------------------------------------------------------------


fun procesarSolicitud(catalogo: List<Sala>, estado: EstadoSistema, solicitud: Solicitud): EstadoSistema {
    val salaEncontrada = buscarSalaDisponible(catalogo, solicitud, estado.asignaciones)
    return if (salaEncontrada != null) {
        //Concepto diferencial 3: estado.copy(asignaciones = estado.asignaciones + Asignacion(...)) en vez de lista.add(...)
        //(datos inmutables)
        estado.copy(asignaciones = estado.asignaciones + Asignacion(solicitud, salaEncontrada))
    } else {
        val motivo = determinarMotivoRechazo(catalogo, solicitud, estado.asignaciones)
        estado.copy(rechazos = estado.rechazos + Rechazo(solicitud, motivo))
    }
}

//Concepto diferencial 1:procesarFlujo con fold en vez de un bucle con acumulador mutable
//(el estado no existe como variable)
fun procesarFlujo(catalogo: List<Sala>, solicitudes: List<Solicitud>): EstadoSistema =
    solicitudes.fold(EstadoSistema()) { estadoAcumulado, solicitud ->
        procesarSolicitud(catalogo, estadoAcumulado, solicitud)
    }

// ---------------------------------------------------------------------
// Informe final
// ---------------------------------------------------------------------

fun generarInforme(estado: EstadoSistema): String = buildString {
    appendLine("===== INFORME DE ASIGNACIÓN DE SALAS =====")
    appendLine()
    appendLine("✔ Solicitudes ACEPTADAS (${estado.asignaciones.size}):")
    if (estado.asignaciones.isEmpty()) {
        appendLine("  (ninguna)")
    } else {
        estado.asignaciones.forEach { a ->
            appendLine(
                "  - [${a.solicitud.id}] ${a.solicitud.franja} · " +
                        "${a.solicitud.asistentes} asistentes → Sala '${a.sala.nombre}'"
            )
        }
    }
    appendLine()
    appendLine("✘ Solicitudes RECHAZADAS (${estado.rechazos.size}):")
    if (estado.rechazos.isEmpty()) {
        appendLine("  (ninguna)")
    } else {
        estado.rechazos.forEach { r ->
            appendLine(
                "  - [${r.solicitud.id}] ${r.solicitud.franja} · " +
                        "${r.solicitud.asistentes} asistentes → Motivo: ${r.motivo}"
            )
        }
    }
}

// ---------------------------------------------------------------------
// Demostración
// ---------------------------------------------------------------------

fun main() {
    val catalogo = listOf(
        Sala("Sala A", capacidad = 4, equipamiento = setOf("pizarra")),
        Sala("Sala B", capacidad = 10, equipamiento = setOf("proyector", "pizarra")),
        Sala("Sala C", capacidad = 20, equipamiento = setOf("proyector", "videoconferencia", "pizarra")),
    )

    val solicitudes = listOf(
        Solicitud("S1", Franja(9 * 60, 10 * 60), asistentes = 3, equipoRequerido = setOf("pizarra")),
        Solicitud("S2", Franja(9 * 60, 10 * 60), asistentes = 6, equipoRequerido = setOf("proyector")),
        Solicitud("S3", Franja(9 * 60 + 30, 10 * 60 + 30), asistentes = 8, equipoRequerido = setOf("proyector")),
        Solicitud("S4", Franja(11 * 60, 12 * 60), asistentes = 15, equipoRequerido = setOf("videoconferencia")),
        Solicitud("S5", Franja(9 * 60, 9 * 60 + 45), asistentes = 25, equipoRequerido = emptySet()),
        Solicitud("S6", Franja(14 * 60, 15 * 60), asistentes = 5, equipoRequerido = setOf("videoconferencia")),
    )

    val estadoFinal = procesarFlujo(catalogo, solicitudes)
    println(generarInforme(estadoFinal))
}
