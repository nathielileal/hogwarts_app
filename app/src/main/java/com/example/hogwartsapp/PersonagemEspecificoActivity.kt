package com.example.hogwartsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hogwartsapp.api.HpApiService
import com.example.hogwartsapp.models.Personagem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonagemEspecificoActivity : AppCompatActivity() {

    private lateinit var spinnerPersonagens: Spinner
    private lateinit var textoNome: TextView
    private lateinit var textoEspecie: TextView
    private lateinit var textoCasa: TextView
    private lateinit var textoApelidos: TextView
    private lateinit var imagemPersonagem: ImageView
    private lateinit var listaPersonagens: List<Personagem>

    private lateinit var api: HpApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem_especifico)

        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(HpApiService::class.java)

        spinnerPersonagens = findViewById(R.id.spinnerPersonagens)
        textoNome = findViewById(R.id.nomePersonagem)
        textoEspecie = findViewById(R.id.especiePersonagem)
        textoCasa = findViewById(R.id.casaPersonagem)
        textoApelidos = findViewById(R.id.apelidosPersonagem)
        imagemPersonagem = findViewById(R.id.imagemPersonagem)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                listaPersonagens = api.buscarPersonagens()

                val nomes = listaPersonagens.map { it.nome }

                val adapter = ArrayAdapter(
                    this@PersonagemEspecificoActivity,
                    android.R.layout.simple_spinner_item,
                    nomes
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerPersonagens.adapter = adapter

                spinnerPersonagens.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: android.view.View?,
                        position: Int,
                        id: Long
                    ) {
                        val personagemSelecionado = listaPersonagens[position]

                        val idPersonagem = personagemSelecionado.id

                        Log.d("Personagem", "ID do personagem selecionado: $idPersonagem")

                        if (idPersonagem != null) {
                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    val personagemDetalhado = api.buscarPersonagemPorId(idPersonagem)
                                    val personagem = personagemDetalhado.firstOrNull()

                                    Log.d("Personagem", "Nome do personagem selecionado: ${personagem?.nome}")

                                    if (personagem != null) {
                                        textoNome.text = personagem.nome
                                        textoEspecie.text = "Espécie: ${personagem.especie}"
                                        textoCasa.text = "Casa: ${personagem.casa ?: "Indefinida"}"
                                        textoApelidos.text = "Apelidos: ${personagem.apelidos?.joinToString(", ") ?: "Nenhum"}"

                                        if (!personagem.foto.isNullOrBlank()) {
                                            imagemPersonagem.visibility = android.view.View.VISIBLE
                                            Glide.with(this@PersonagemEspecificoActivity)
                                                .load(personagem.foto)
                                                .into(imagemPersonagem)
                                        } else {
                                            imagemPersonagem.visibility = android.view.View.GONE
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("Personagem", "Erro ao buscar personagem detalhado", e)
                                    textoNome.text = "Erro ao carregar personagem."
                                    textoEspecie.text = ""
                                    textoCasa.text = ""
                                    textoApelidos.text = ""
                                    imagemPersonagem.visibility = android.view.View.GONE
                                }
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        textoNome.text = ""
                        textoEspecie.text = ""
                        textoCasa.text = ""
                        textoApelidos.text = ""
                        imagemPersonagem.visibility = android.view.View.GONE
                    }
                }

            } catch (e: Exception) {
                textoNome.text = "Erro ao carregar lista de personagens."
            }
        }
    }
}