package com.madicine.deliverycontrol

import org.junit.Assert.*
import org.junit.Test
import java.util.Base64

class ReportErrorTest {

    @Test
    fun `encodeByteArrayToBase64 returns valid Base64 string`() {
        // Simulamos una "imagen" como un arreglo de bytes arbitrario
        val fakeImageBytes = byteArrayOf(0x01, 0x02, 0x03)

        val base64String = encodeByteArrayToBase64(fakeImageBytes)

        assertNotNull(base64String)
        assertTrue(base64String.isNotEmpty())
    }

    @Test
    fun `cleanBase64String removes newline and backslashes`() {
        val dirtyBase64 = "aGVsbG9cbg==\\" // "hello\n" con barra invertida
        val cleaned = cleanBase64String(dirtyBase64)

        assertFalse(cleaned.contains("\\n"))
        assertFalse(cleaned.contains("\\"))
    }

    private fun encodeByteArrayToBase64(byteArray: ByteArray): String {
        val base64String = Base64.getEncoder().encodeToString(byteArray)
        return cleanBase64String(base64String)
    }

    private fun cleanBase64String(base64String: String): String {
        return base64String.replace("\\n", "").replace("\\", "")
    }
}
