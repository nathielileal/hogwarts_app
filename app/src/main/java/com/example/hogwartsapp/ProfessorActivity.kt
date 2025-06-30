package com.example.hogwartsapp

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hogwartsapp.api.HpApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfessorActivity : AppCompatActivity() {

    private lateinit var api: HpApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor)

        val spinnerProfessores = findViewById<Spinner>(R.id.spinnerProfessores)
        val imagem = findViewById<ImageView>(R.id.imagemProfessor)
        val nomeTexto = findViewById<TextView>(R.id.nomeProfessor)
        val apelidosTexto = findViewById<TextView>(R.id.apelidosProfessor)
        val especieTexto = findViewById<TextView>(R.id.especieProfessor)
        val casaTexto = findViewById<TextView>(R.id.casaProfessor)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(HpApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val professores = api.buscarProfessores()

                val nomes = professores.map { it.nome }

                val adapter = ArrayAdapter(
                    this@ProfessorActivity,
                    android.R.layout.simple_spinner_item,
                    nomes
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerProfessores.adapter = adapter

                spinnerProfessores.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: android.view.View?,
                            position: Int,
                            id: Long
                        ) {
                            val professor = professores[position]

                            nomeTexto.text = professor.nome
                            apelidosTexto.text = professor.apelidos?.joinToString(", ") ?: "Nenhum"
                            especieTexto.text = "Espécie: ${professor.especie}"
                            casaTexto.text = "Casa: ${professor.casa ?: "Indefinida"}"

                            if (!professor.foto.isNullOrBlank()) {
                                imagem.visibility = android.view.View.VISIBLE
                                Glide.with(this@ProfessorActivity)
                                    .load(professor.foto)
                                    .into(imagem)
                            } else {
                                imagem.visibility = android.view.View.GONE
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            nomeTexto.text = ""
                            apelidosTexto.text = ""
                            especieTexto.text = ""
                            casaTexto.text = ""
                            imagem.visibility = android.view.View.GONE
                        }
                    }

            } catch (e: Exception) {
                Toast.makeText(
                    this@ProfessorActivity,
                    "Erro ao carregar professores.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}