package com.marceloacuna.myapksemana8.Model


import kotlinx.serialization.Serializable

@Serializable
data class User_Model (
    val email:String = "",
    val contraseña: String = "",
)