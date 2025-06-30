package com.example.hogwartsapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwartsapp.adapter.PersonagemAdapter
import com.example.hogwartsapp.api.HpApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CasaActivity : AppCompatActivity() {

    private lateinit var spinnerCasas: Spinner
    private lateinit var recyclerAlunos: RecyclerView
    private lateinit var adapter: PersonagemAdapter

    private lateinit var api: HpApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_casa)

        spinnerCasas = findViewById(R.id.spinnerCasas)
        recyclerAlunos = findViewById(R.id.recyclerAlunos)

        adapter = PersonagemAdapter(emptyList())
        recyclerAlunos.layoutManager = LinearLayoutManager(this)
        recyclerAlunos.adapter = adapter

        val casas = listOf("gryffindor", "slytherin", "hufflepuff", "ravenclaw")
        val nomesCasas = listOf("Grifinória", "Sonserina", "Lufa-Lufa", "Corvinal")

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomesCasas)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCasas.adapter = spinnerAdapter

        spinnerCasas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val casaSelecionada = casas[position]
                buscarAlunosPorCasa(casaSelecionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                adapter.atualizarLista(emptyList())
            }
        }

        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun buscarAlunosPorCasa(casa: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(HpApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val alunos = api.buscarPersonagensPorCasa(casa)
                adapter.atualizarLista(alunos)
            } catch (e: Exception) {
                adapter.atualizarLista(emptyList())
            }
        }
    }
}