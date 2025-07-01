package com.example.hogwartsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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