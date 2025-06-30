package com.example.hogwartsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hogwartsapp.models.Personagem
import com.example.hogwartsapp.R

class PersonagemAdapter(private var lista: List<Personagem>) :
    RecyclerView.Adapter<PersonagemAdapter.PersonagemViewHolder>() {

    class PersonagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem: ImageView = itemView.findViewById(R.id.imgPersonagem)
        val nome: TextView = itemView.findViewById(R.id.txtNome)
        val apelidos: TextView = itemView.findViewById(R.id.txtApelidos)
        val especie: TextView = itemView.findViewById(R.id.txtEspecie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonagemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.personagem, parent, false)
        return PersonagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonagemViewHolder, position: Int) {
        val personagem = lista[position]

        holder.nome.text = personagem.nome
        holder.apelidos.text = "Apelidos: ${personagem.apelidos?.joinToString(", ") ?: "Nenhum"}"
        holder.especie.text = "Espécie: ${personagem.especie}"

        if (!personagem.foto.isNullOrEmpty()) {
            holder.imagem.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(personagem.foto).into(holder.imagem)
        } else {
            holder.imagem.visibility = View.GONE
        }
    }

    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Personagem>) {
        lista = novaLista
        notifyDataSetChanged()
    }
}
