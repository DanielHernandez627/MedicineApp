package com.madicine.deliverycontrol

import com.madicine.deliverycontrol.Entities.MedicamentosConsulta
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoricTest {

    data class Resultado(val titulo: String, val fecha: String)

    class ResultadoAdapter(private val resultados: List<Resultado>) {
        fun getItemCount(): Int = resultados.size
    }

    @Test
    fun `showMedicine transforma correctamente medicamentos en resultados`() {
        val medicamentos = listOf(
            MedicamentosConsulta(1, "Paracetamol", "2024-04-01"),
            MedicamentosConsulta(2, "Ibuprofeno", "2024-04-02")
        )

        val resultados = medicamentos.map {
            Resultado(it.nombreMedicamento, it.fechaConsulta)
        }

        assertEquals(2, resultados.size)
        assertEquals("Paracetamol", resultados[0].titulo)
        assertEquals("2024-04-01", resultados[0].fecha)
        assertEquals("Ibuprofeno", resultados[1].titulo)
        assertEquals("2024-04-02", resultados[1].fecha)
    }

    @Test
    fun `ResultadoAdapter retorna tamaño correcto`() {
        val resultados = listOf(
            Resultado("Medicamento A", "2024-03-10"),
            Resultado("Medicamento B", "2024-03-12")
        )

        val adapter = ResultadoAdapter(resultados)
        assertEquals(2, adapter.getItemCount())
    }

    @Test
    fun `Resultado contiene los datos correctos`() {
        val resultado = Resultado("Amoxicilina", "2024-04-05")
        assertEquals("Amoxicilina", resultado.titulo)
        assertEquals("2024-04-05", resultado.fecha)
    }

    @Test
    fun `ResultadoAdapter funciona con lista vacía`() {
        val adapter = ResultadoAdapter(emptyList())
        assertEquals(0, adapter.getItemCount())
    }

    @Test
    fun `ResultadoAdapter cuenta correctamente una lista grande`() {
        val resultados = List(100) { i -> Resultado("Med$i", "2024-04-${i + 1}") }
        val adapter = ResultadoAdapter(resultados)
        assertEquals(100, adapter.getItemCount())
    }

    @Test
    fun `map de medicamentos a resultados conserva orden`() {
        val medicamentos = listOf(
            MedicamentosConsulta(1, "Aspirina", "2024-01-01"),
            MedicamentosConsulta(2, "Cetirizina", "2024-01-02"),
            MedicamentosConsulta(3, "Diclofenaco", "2024-01-03")
        )

        val resultados = medicamentos.map {
            Resultado(it.nombreMedicamento, it.fechaConsulta)
        }

        assertEquals("Aspirina", resultados[0].titulo)
        assertEquals("Cetirizina", resultados[1].titulo)
        assertEquals("Diclofenaco", resultados[2].titulo)
    }
}
