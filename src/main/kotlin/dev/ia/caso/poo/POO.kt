package dev.ia.caso.poo

// Asistido por IA
// Versión Orientada a Objetos del sistema de asignación de salas.
//
// Conceptos diferenciales frente a la versión funcional:
// 1) Sala encapsula su PROPIO estado de ocupación (reservas) en vez de recibir
//    la lista global de asignaciones como parámetro externo (transparencia referencial perdida
//    a cambio de encapsulación: cada objeto es responsable de sus propios datos).
// 2) La mutación es un efecto secundario controlado a través de métodos (sala.reservar(...)),
//    no la creación de una nueva estructura inmutable.
// 3) El "estado del sistema" no es un dato inmutable que se reemplaza en cada paso,
//    sino el estado interno (privado) de un objeto (SistemaAsignacion) que se muta con cada llamada.
// 4) Bucle for tradicional operando sobre estado mutable, en vez de fold con estados intermedios.
// 5) Las reglas de negocio (capacidad, equipamiento, ocupación) son MÉTODOS de Sala,
//    es decir, mensajes que se le envían al objeto, no funciones puras externas que reciben la sala como dato.

class Franja(val inicio: Int, val fin: Int) {
    init {
        require(inicio < fin) { "La franja debe tener inicio < fin" }
    }

    fun seSolapaCon(otra: Franja): Boolean =
        inicio < otra.fin && otra.inicio < fin

    override fun toString(): String = "%02d:%02d-%02d:%02d".format(
        inicio / 60, inicio % 60, fin / 60, fin % 60
    )
}

class Solicitud(
    val id: String,
    val franja: Franja,
    val asistentes: Int,
    val equipoRequerido: Set<String>
)

// La Sala es un objeto "activo": conoce sus propias reglas y mantiene su propio historial.
class Sala(
    val nombre: String,
    private val capacidad: Int,
    private val equipamiento: Set<String>
) {
    private val reservas = mutableListOf<Solicitud>()

    fun tieneCapacidadPara(solicitud: Solicitud): Boolean =
        capacidad >= solicitud.asistentes

    fun tieneEquipamientoPara(solicitud: Solicitud): Boolean =
        equipamiento.containsAll(solicitud.equipoRequerido)

    fun estaOcupadaEn(franja: Franja): Boolean =
        reservas.any { it.franja.seSolapaCon(franja) }

    fun puedeAtender(solicitud: Solicitud): Boolean =
        tieneCapacidadPara(solicitud) &&
                tieneEquipamientoPara(solicitud) &&
                !estaOcupadaEn(solicitud.franja)

    // Mutación encapsulada: nadie fuera de Sala puede modificar `reservas` directamente.
    fun reservar(solicitud: Solicitud) {
        reservas.add(solicitud)
    }
}

class Asignacion(val solicitud: Solicitud, val sala: Sala)
class Rechazo(val solicitud: Solicitud, val motivo: String)

// El "cerebro" del sistema: orquesta el flujo y guarda su propio estado internamente.
class SistemaAsignacion(private val catalogo: List<Sala>) {

    val asignaciones: List<Asignacion>
        field = mutableListOf()

    val rechazos: List<Rechazo>
        field = mutableListOf()

    private fun buscarSalaDisponible(solicitud: Solicitud): Sala? =
        catalogo.firstOrNull { it.puedeAtender(solicitud) }

    private fun determinarMotivoRechazo(solicitud: Solicitud): String {
        val ningunaTieneCapacidad = catalogo.none { it.tieneCapacidadPara(solicitud) }
        val ningunaTieneEquipo = catalogo.none { it.tieneEquipamientoPara(solicitud) }
        val factibles = catalogo.filter {
            it.tieneCapacidadPara(solicitud) && it.tieneEquipamientoPara(solicitud)
        }

        return when {
            ningunaTieneCapacidad ->
                "Ninguna sala tiene capacidad suficiente para ${solicitud.asistentes} asistentes"

            ningunaTieneEquipo ->
                "Ninguna sala cuenta con el equipamiento requerido: ${solicitud.equipoRequerido.joinToString()}"

            factibles.isEmpty() ->
                "Ninguna sala cumple simultáneamente la capacidad y el equipamiento requeridos"

            factibles.all { it.estaOcupadaEn(solicitud.franja) } ->
                "Las salas que cumplen los requisitos (${factibles.joinToString { s -> s.nombre }}) " +
                        "están ocupadas en la franja ${solicitud.franja}"

            else ->
                "No fue posible asignar una sala por restricciones combinadas"
        }
    }

    // Procesar una solicitud MUTA el estado interno (efecto secundario), no retorna un nuevo estado.
    fun procesarSolicitud(solicitud: Solicitud) {
        val sala = buscarSalaDisponible(solicitud)
        if (sala != null) {
            sala.reservar(solicitud)
            asignaciones.add(Asignacion(solicitud, sala)) // smart cast a MutableList aquí dentro
        } else {
            rechazos.add(Rechazo(solicitud, determinarMotivoRechazo(solicitud)))
        }
    }

    fun procesarFlujo(solicitudes: List<Solicitud>) {
        for (solicitud in solicitudes) {
            procesarSolicitud(solicitud)
        }
    }

    fun generarInforme(): String = buildString {
        appendLine("===== INFORME DE ASIGNACIÓN DE SALAS =====")
        appendLine()
        appendLine("✔ Solicitudes ACEPTADAS (${asignaciones.size}):")
        if (asignaciones.isEmpty()) {
            appendLine("  (ninguna)")
        } else {
            asignaciones.forEach { a ->
                appendLine(
                    "  - [${a.solicitud.id}] ${a.solicitud.franja} · " +
                            "${a.solicitud.asistentes} asistentes → Sala '${a.sala.nombre}'"
                )
            }
        }
        appendLine()
        appendLine("✘ Solicitudes RECHAZADAS (${rechazos.size}):")
        if (rechazos.isEmpty()) {
            appendLine("  (ninguna)")
        } else {
            rechazos.forEach { r ->
                appendLine(
                    "  - [${r.solicitud.id}] ${r.solicitud.franja} · " +
                            "${r.solicitud.asistentes} asistentes → Motivo: ${r.motivo}"
                )
            }
        }
    }
}


// Demostración
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

    val sistema = SistemaAsignacion(catalogo)
    sistema.procesarFlujo(solicitudes)
    println(sistema.generarInforme())
}
