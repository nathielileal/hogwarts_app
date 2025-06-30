package com.example.hogwartsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwartsapp.adapter.FeiticoAdapter
import com.example.hogwartsapp.api.HpApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeiticoActivity : AppCompatActivity() {

    private lateinit var recyclerFeiticos: RecyclerView
    private lateinit var adapter: FeiticoAdapter
    private lateinit var api: HpApiService

    private lateinit var layoutDetalhes: LinearLayout
    private lateinit var btnVoltar: Button
    private lateinit var btnVoltarLista: Button
    private lateinit var textNomeDetalhe: TextView
    private lateinit var textDescricaoDetalhe: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feitico)

        recyclerFeiticos = findViewById(R.id.recyclerFeiticos)
        recyclerFeiticos.layoutManager = LinearLayoutManager(this)

        layoutDetalhes = findViewById(R.id.layoutDetalhes)
        btnVoltar = findViewById(R.id.btnVoltar)
        btnVoltarLista = findViewById(R.id.btnVoltarLista)
        textNomeDetalhe = findViewById(R.id.textNomeDetalhe)
        textDescricaoDetalhe = findViewById(R.id.textDescricaoDetalhe)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnVoltarLista.visibility = View.GONE

        btnVoltarLista.setOnClickListener {
            layoutDetalhes.visibility = View.GONE
            btnVoltarLista.visibility = View.GONE

            recyclerFeiticos.visibility = View.VISIBLE
            btnVoltar.visibility = View.VISIBLE
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(HpApiService::class.java)

        carregarFeiticos()
    }

    private fun carregarFeiticos() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val lista = api.buscarFeiticos()

                adapter = FeiticoAdapter(lista) { feiticoSelecionado ->
                    recyclerFeiticos.visibility = View.GONE
                    layoutDetalhes.visibility = View.VISIBLE

                    btnVoltar.visibility = View.GONE
                    btnVoltarLista.visibility = View.VISIBLE

                    textNomeDetalhe.text = feiticoSelecionado.nome
                    textDescricaoDetalhe.text = feiticoSelecionado.descricao
                }

                recyclerFeiticos.adapter = adapter
            } catch (e: Exception) {
                textNomeDetalhe.text = "Erro ao carregar feitiços."
                textDescricaoDetalhe.text = ""
                recyclerFeiticos.visibility = View.GONE
                layoutDetalhes.visibility = View.VISIBLE
            }
        }
    }
}