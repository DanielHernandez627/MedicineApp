package com.madicine.deliverycontrol

import com.madicine.deliverycontrol.Services.SessionService
import com.madicine.deliverycontrol.Repositories.SharedPrefsRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class MenuPrincipalTest {

    private lateinit var mockSharedPrefs: SharedPrefsRepository
    private lateinit var sessionService: SessionService

    @Before
    fun setUp() {
        mockSharedPrefs = mock()
        sessionService = SessionService(mockSharedPrefs)
    }

    @Test
    fun `saveSessionData calls putString on SharedPrefsRepository`() {
        val name = "Juan Pérez"
        val email = "juan@mail.com"
        val uid = "123ABC"

        sessionService.saveSessionData(name, email, uid)

        verify(mockSharedPrefs).putString("nombre", name)
        verify(mockSharedPrefs).putString("email", email)
        verify(mockSharedPrefs).putString("uid", uid)
    }

    @Test
    fun `getNombre returns stored value`() {
        whenever(mockSharedPrefs.getString("nombre")).thenReturn("Juan Pérez")
        assertEquals("Juan Pérez", sessionService.getNombre())
    }

    @Test
    fun `getEmail returns stored value`() {
        whenever(mockSharedPrefs.getString("email")).thenReturn("juan@mail.com")
        assertEquals("juan@mail.com", sessionService.getEmail())
    }

    @Test
    fun `getUid returns stored value`() {
        whenever(mockSharedPrefs.getString("uid")).thenReturn("123ABC")
        assertEquals("123ABC", sessionService.getUid())
    }

    @Test
    fun `clearSession calls clear on SharedPrefsRepository`() {
        sessionService.clearSession()
        verify(mockSharedPrefs).clear()
    }
}
