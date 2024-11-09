package com.madicine.deliverycontrol.Services

import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

object UserService {

    fun createUser(nombre:String,apellido:String,uuid:String): String? {
        val endpoint = "https://apimdcs-production.up.railway.app/api/usuarios"
        val url = URL(endpoint)
        val connection = url.openConnection() as HttpURLConnection

        //Fecha de consulta
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(System.currentTimeMillis())

        return try {
            // Configura la conexión para POST
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            // Crea el objeto JSON con los datos
            val jsonObject = JSONObject()
            jsonObject.put("U_Uid", uuid)
            jsonObject.put("Nombre", nombre)
            jsonObject.put("Apellido", apellido)
            jsonObject.put("FechaIngreso", currentDate)

            // Escribe el JSON en el OutputStream
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonObject.toString())
                writer.flush()
            }

            // Verifica la respuesta del servidor
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta
                connection.inputStream.bufferedReader().use { reader ->
                    reader.readText() // Retorna la respuesta del servidor
                }
            } else {
                null // Indica un error en la respuesta
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // En caso de una excepción, retorna null
        } finally {
            connection.disconnect()
        }
    }

}