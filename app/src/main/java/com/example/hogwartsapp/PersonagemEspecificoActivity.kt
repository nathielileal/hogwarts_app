package com.example.hogwartsapp

import android.os.Bundle
import android.view.View
import android.widget.*
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

    private lateinit var api: HpApiService
    private lateinit var spinner: Spinner
    private lateinit var imagem: ImageView
    private lateinit var nomeTexto: TextView
    private lateinit var apelidosTexto: TextView
    private lateinit var especieTexto: TextView
    private lateinit var casaTexto: TextView
    private lateinit var campoBusca: EditText
    private lateinit var btnPesquisar: Button
    private lateinit var btnLimpar: Button
    private lateinit var adapter: ArrayAdapter<String>

    private var listaPersonagens: List<Personagem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem_especifico)

        spinner = findViewById(R.id.spinnerPersonagens)
        imagem = findViewById(R.id.imagemPersonagem)
        nomeTexto = findViewById(R.id.nomePersonagem)
        apelidosTexto = findViewById(R.id.apelidosPersonagem)
        especieTexto = findViewById(R.id.especiePersonagem)
        casaTexto = findViewById(R.id.casaPersonagem)
        campoBusca = findViewById(R.id.edtNome)
        btnPesquisar = findViewById(R.id.btnPesquisar)
        btnLimpar = findViewById(R.id.btnLimpar)

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        esconderDados()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(HpApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                listaPersonagens = api.buscarPersonagens().sortedBy { it.nome }

                val nomes = listOf("Ver lista com os nomes em ordem") + listaPersonagens.map { it.nome }

                adapter = ArrayAdapter(
                    this@PersonagemEspecificoActivity,
                    android.R.layout.simple_spinner_item,
                    nomes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(this@PersonagemEspecificoActivity, "Erro ao carregar lista.", Toast.LENGTH_LONG).show()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) return
                val nomeSelecionado = adapter.getItem(position)
                campoBusca.setText(nomeSelecionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnPesquisar.setOnClickListener {
            val termo = campoBusca.text.toString().trim()
            esconderDados()

            if (termo.isEmpty()) {
                nomeTexto.text = "Dados não encontrados"
                nomeTexto.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val encontrados = listaPersonagens.filter {
                it.nome.contains(termo, ignoreCase = true)
            }

            if (encontrados.isEmpty()) {
                nomeTexto.text = "Dados não encontrados"
                nomeTexto.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val personagem = encontrados.first()

            nomeTexto.text = personagem.nome
            apelidosTexto.text = "Apelidos: ${personagem.apelidos?.joinToString(", ") ?: "Nenhum"}"
            especieTexto.text = "Espécie: ${personagem.especie}"
            casaTexto.text = "Casa: ${personagem.casa ?: "Indefinida"}"

            nomeTexto.visibility = View.VISIBLE
            apelidosTexto.visibility = View.VISIBLE
            especieTexto.visibility = View.VISIBLE
            casaTexto.visibility = View.VISIBLE

            if (!personagem.foto.isNullOrBlank()) {
                imagem.visibility = View.VISIBLE
                Glide.with(this).load(personagem.foto).into(imagem)
            }
        }

        btnLimpar.setOnClickListener {
            campoBusca.setText("")
            spinner.setSelection(0)
            esconderDados()
        }
    }

    private fun esconderDados() {
        nomeTexto.visibility = View.GONE
        apelidosTexto.visibility = View.GONE
        especieTexto.visibility = View.GONE
        casaTexto.visibility = View.GONE
        imagem.visibility = View.GONE
    }
}
