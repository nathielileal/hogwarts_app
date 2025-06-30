package com.example.hogwartsapp.models

import com.google.gson.annotations.SerializedName

data class Feitico(
    val id: String? = null,

    @SerializedName("name")
    val nome: String,

    @SerializedName("description")
    val descricao: String
)
