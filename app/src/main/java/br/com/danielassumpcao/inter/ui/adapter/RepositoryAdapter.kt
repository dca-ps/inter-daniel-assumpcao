package br.com.danielassumpcao.inter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.ui.listeners.RepositoryClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView

class RepositoryAdapter(private val dataSet: List<Repository>, val context: Context?, val listener: RepositoryClickListener): RecyclerView.Adapter<RepositoryAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryAdapter.ViewHolder, position: Int) {
        val currentItem: Repository = dataSet.get(position)

        context?.let {
            Glide.with(it)
                .load(currentItem.owner.avatar_url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.shapeableImageView);
        }

        holder.repositoryNameTV.text = currentItem.name
        holder.repostioryDescriptionTV.text = currentItem.description
        holder.pullRequestNumberTV.text = currentItem.forks_count.toString()
        holder.startNumberTV.text = currentItem.stargazers_count.toString()
        holder.userIdTV.text = currentItem.owner.login

        holder.containerSV.setOnClickListener { listener.onRepositoryClick(currentItem) }

    }

    override fun getItemCount() = dataSet.size



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repositoryNameTV: TextView
        val repostioryDescriptionTV: TextView
        val pullRequestNumberTV: TextView
        val startNumberTV: TextView
        val userIdTV: TextView
        val shapeableImageView: ImageView
        val containerSV: ConstraintLayout


        init{
            repositoryNameTV = itemView.findViewById(R.id.repositoryNameTV)
            repostioryDescriptionTV = itemView.findViewById(R.id.repostioryDescriptionTV)
            pullRequestNumberTV = itemView.findViewById(R.id.pullRequestNumberTV)
            startNumberTV = itemView.findViewById(R.id.startNumberTV)
            userIdTV = itemView.findViewById(R.id.userIdTV)
            shapeableImageView = itemView.findViewById(R.id.shapeableImageView)
            containerSV = itemView.findViewById(R.id.containerSV)

        }

    }
}