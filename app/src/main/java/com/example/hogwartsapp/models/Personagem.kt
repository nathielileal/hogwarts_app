package com.example.hogwartsapp.models

import com.google.gson.annotations.SerializedName

data class Personagem(
    val id: String? = null,

    @SerializedName("name")
    val nome: String,

    @SerializedName("species")
    val especie: String,

    @SerializedName("house")
    val casa: String?,

    @SerializedName("alternate_names")
    val apelidos: List<String>?,

    @SerializedName("image")
    val foto: String?
)
