package com.marceloacuna.myapksemana8.Model

import kotlinx.serialization.Serializable

@Serializable
data class Letras_Model(
    val nombre:String = "",
    val descripcion: String = "",
    val imgen: String = "")