package com.madicine.deliverycontrol.Services

import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object HistoryService {

    fun searchHistoryByUuid(uuid: String): String? {

        val endpoint = "https://apimdcs-production.up.railway.app/api/medicamentos/historial"
        val url = URL(endpoint)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            //Definicion del metodo
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            // Crea el objeto JSON con los datos
            val jsonObject = JSONObject()
            jsonObject.put("U_Uid", uuid)
            jsonObject.put("top",10)

            // Escribe el JSON en el OutputStream
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonObject.toString())
                writer.flush()
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta
                connection.inputStream.bufferedReader().use { reader ->
                    reader.readText() // Retorna la respuesta del servidor
                }
            } else {
                null // Indica un error en la respuesta
            }

        }catch (e: Exception){
            e.printStackTrace()
            null
        }finally {
            null
        }

    }

}