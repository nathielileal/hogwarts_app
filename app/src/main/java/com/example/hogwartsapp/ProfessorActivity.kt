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

class ProfessorActivity : AppCompatActivity() {

    private lateinit var api: HpApiService
    private var professores: List<Personagem> = listOf()
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor)

        val spinner = findViewById<Spinner>(R.id.spinnerProfessores)
        val imagem = findViewById<ImageView>(R.id.imagemProfessor)
        val nomeTexto = findViewById<TextView>(R.id.nomeProfessor)
        val apelidosTexto = findViewById<TextView>(R.id.apelidosProfessor)
        val especieTexto = findViewById<TextView>(R.id.especieProfessor)
        val casaTexto = findViewById<TextView>(R.id.casaProfessor)
        val edtNome = findViewById<EditText>(R.id.edtNomeProfessor)
        val btnPesquisar = findViewById<Button>(R.id.btnPesquisarProfessor)
        val btnLimpar = findViewById<Button>(R.id.btnLimparBusca)

        esconderDados(imagem, nomeTexto, apelidosTexto, especieTexto, casaTexto)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(HpApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                professores = api.buscarProfessores().sortedBy { it.nome }
                val nomes = listOf("Ver lista com os nomes em ordem") + professores.map { it.nome }

                spinnerAdapter = ArrayAdapter(
                    this@ProfessorActivity,
                    android.R.layout.simple_spinner_item,
                    nomes
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = spinnerAdapter

            } catch (e: Exception) {
                Toast.makeText(this@ProfessorActivity, "Erro ao carregar professores.", Toast.LENGTH_LONG).show()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) return
                val nomeSelecionado = spinnerAdapter.getItem(position)
                edtNome.setText(nomeSelecionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnPesquisar.setOnClickListener {
            val nomeBuscado = edtNome.text.toString().trim()
            if (nomeBuscado.isEmpty()) {
                Toast.makeText(this, "Digite um nome para buscar.", Toast.LENGTH_SHORT).show()
                esconderDados(imagem, nomeTexto, apelidosTexto, especieTexto, casaTexto)
                nomeTexto.text = "Dados não encontrados"
                nomeTexto.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val encontrados = professores.filter {
                it.nome.contains(nomeBuscado, ignoreCase = true)
            }

            if (encontrados.isEmpty()) {
                esconderDados(imagem, nomeTexto, apelidosTexto, especieTexto, casaTexto)
                nomeTexto.text = "Dados não encontrados"
                nomeTexto.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val professor = encontrados.first()

            nomeTexto.text = professor.nome
            apelidosTexto.text = professor.apelidos?.joinToString(", ") ?: "Nenhum"
            especieTexto.text = "Espécie: ${professor.especie}"
            casaTexto.text = "Casa: ${professor.casa ?: "Indefinida"}"

            nomeTexto.visibility = View.VISIBLE
            apelidosTexto.visibility = View.VISIBLE
            especieTexto.visibility = View.VISIBLE
            casaTexto.visibility = View.VISIBLE

            if (!professor.foto.isNullOrBlank()) {
                imagem.visibility = View.VISIBLE
                Glide.with(this).load(professor.foto).into(imagem)
            } else {
                imagem.visibility = View.GONE
            }
        }

        btnLimpar.setOnClickListener {
            edtNome.setText("")
            spinner.setSelection(0)
            esconderDados(imagem, nomeTexto, apelidosTexto, especieTexto, casaTexto)
        }

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish()
        }
    }

    private fun esconderDados(
        imagem: ImageView,
        nome: TextView,
        apelidos: TextView,
        especie: TextView,
        casa: TextView
    ) {
        imagem.visibility = View.GONE
        nome.visibility = View.GONE
        apelidos.visibility = View.GONE
        especie.visibility = View.GONE
        casa.visibility = View.GONE
    }
}
