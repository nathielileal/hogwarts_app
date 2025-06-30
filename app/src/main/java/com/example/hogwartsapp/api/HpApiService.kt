package com.example.hogwartsapp.api

import com.example.hogwartsapp.models.Feitico
import com.example.hogwartsapp.models.Personagem
import retrofit2.http.GET
import retrofit2.http.Path

interface HpApiService {
    @GET("api/characters")
    suspend fun buscarPersonagens(): List<Personagem>

    @GET("api/character/{id}")
    suspend fun buscarPersonagemPorId(@Path("id") id: String): List<Personagem>

    @GET("api/characters/staff")
    suspend fun buscarProfessores(): List<Personagem>

    @GET("api/characters/house/{house}")
    suspend fun buscarPersonagensPorCasa(@Path("house") casa: String): List<Personagem>

    @GET("api/spells")
    suspend fun buscarFeiticos(): List<Feitico>
}