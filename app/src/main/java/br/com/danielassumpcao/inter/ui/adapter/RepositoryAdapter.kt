package br.com.danielassumpcao.inter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.models.Repository

class RepositoryAdapter(private val dataSet: List<Repository>): RecyclerView.Adapter<RepositoryAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryAdapter.ViewHolder, position: Int) {
        val currentItem: Repository = dataSet.get(position)

        holder.textView.text = currentItem.name
    }

    override fun getItemCount() = dataSet.size



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView

        init{
            textView = itemView.findViewById(R.id.textView)
        }

    }
}