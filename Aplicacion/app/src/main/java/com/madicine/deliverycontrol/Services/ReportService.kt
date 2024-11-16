package com.madicine.deliverycontrol.Services

import com.madicine.deliverycontrol.Entities.ReporteMedicamentos
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

object ReportService {

    fun createReport(reporteMedicamentos: ReporteMedicamentos): String? {
        val endpoint = "https://apimdcs-production.up.railway.app/api/reportemedicamentos/cargarReporte"
        val url = URL(endpoint)
        var connection: HttpURLConnection? = null

        //Fecha de consulta
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(System.currentTimeMillis())

        return try {
            connection = url.openConnection() as HttpURLConnection

            // Configura la conexión
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val cleanedBase64Image = cleanBase64String(reporteMedicamentos.imagen)

            // Crea el objeto JSON con los datos
            val jsonObject = JSONObject().apply {
                put("descripcion", reporteMedicamentos.descripcion)
                put("imagen", cleanedBase64Image)
                put("fechaReporte", currentDate)
                put("U_Uid", reporteMedicamentos.udi)
            }

            // Escribe el JSON en el OutputStream
            connection.outputStream.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }
            }

            // Verifica la respuesta
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta del servidor
                connection.inputStream.bufferedReader().use { reader ->
                    reader.readText()
                }
            } else {
                null // Si la respuesta no es HTTP_OK, retorna null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            connection?.disconnect() // Asegúrate de cerrar la conexión
        }
    }

    private fun cleanBase64String(base64String: String): String {
        return base64String.replace("\n", "").replace("\r", "")
    }

}