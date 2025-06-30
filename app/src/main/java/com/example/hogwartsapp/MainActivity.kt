package com.example.hogwartsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        findViewById<Button>(R.id.btnPersonagem).setOnClickListener {
            startActivity(Intent(this, PersonagemEspecificoActivity::class.java))
        }

        findViewById<Button>(R.id.btnProfessor).setOnClickListener {
            startActivity(Intent(this, ProfessorActivity::class.java))
        }

        findViewById<Button>(R.id.btnCasa).setOnClickListener {
            startActivity(Intent(this, CasaActivity::class.java))
        }

        findViewById<Button>(R.id.btnFeitico).setOnClickListener {
            startActivity(Intent(this, FeiticoActivity::class.java))
        }

        findViewById<Button>(R.id.btnSair).setOnClickListener {
            finish()
        }
    }
}