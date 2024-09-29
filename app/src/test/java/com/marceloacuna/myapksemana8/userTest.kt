package com.marceloacuna.myapksemana8

import com.marceloacuna.myapksemana8.Model.Letras_Model
import com.marceloacuna.myapksemana8.Model.User_Model
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before

class userTest {
    private lateinit var usermodel : User_Model

    @Before
    fun setUp() {
        usermodel = User_Model("estudiante@gmail.com", "@123gHft")
    }

    @Test
    fun testGetemail() {
        assertEquals("estudiante@gmail.com",usermodel.email, "estudiante@gmail.com")
    }

    @Test
    fun testGetPassword(){
        assertEquals("@123gHft",usermodel.contraseña,"@123gHft")
    }

}