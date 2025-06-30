package com.example.hogwartsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwartsapp.R
import com.example.hogwartsapp.models.Feitico

class FeiticoAdapter(
    private var listaFeiticos: List<Feitico>,
    private val onItemClick: (Feitico) -> Unit
) : RecyclerView.Adapter<FeiticoAdapter.FeiticoViewHolder>() {

    inner class FeiticoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoNome: TextView = itemView.findViewById(R.id.textoNomeFeitico)
        init {
            itemView.setOnClickListener {
                onItemClick(listaFeiticos[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeiticoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feitico, parent, false)
        return FeiticoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeiticoViewHolder, position: Int) {
        val feitico = listaFeiticos[position]
        holder.textoNome.text = feitico.nome
    }

    override fun getItemCount() = listaFeiticos.size

    fun atualizarLista(novaLista: List<Feitico>) {
        listaFeiticos = novaLista
        notifyDataSetChanged()
    }
}
