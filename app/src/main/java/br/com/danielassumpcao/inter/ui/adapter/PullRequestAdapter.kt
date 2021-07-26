package br.com.danielassumpcao.inter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.ui.listeners.PullRequestClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PullRequestAdapter(private val dataSet: List<PullRequest>, val context: Context?, val listener: PullRequestClickListener): RecyclerView.Adapter<PullRequestAdapter.ViewHolder>()   {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pull_request, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem: PullRequest = dataSet.get(position)

        context?.let {
            Glide.with(it)
                .load(currentItem.user.avatar_url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.userAvatarIV);
        }
        holder.userIdTV.text = currentItem.user.login
        holder.pullRequestTitleTV.text = currentItem.title
        holder.pullRequestBodyTV.text = currentItem.body
        holder.pullRequestDateTV.text = LocalDateTime.parse(currentItem.created_at.dropLast(1)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        holder.containerSV.setOnClickListener { listener.onPullRequestClick(currentItem.html_url) }
    }

    override fun getItemCount() = dataSet.size



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pullRequestTitleTV: TextView
        val pullRequestBodyTV: TextView
        val pullRequestDateTV: TextView
        val userIdTV: TextView
        val userAvatarIV: ImageView
        val containerSV: ConstraintLayout


        init{
            pullRequestTitleTV = itemView.findViewById(R.id.pullRequestTitleTV)
            pullRequestBodyTV = itemView.findViewById(R.id.pullRequestBodyTV)
            pullRequestDateTV = itemView.findViewById(R.id.pullRequestDateTV)
            userIdTV = itemView.findViewById(R.id.userIdTV)
            userAvatarIV = itemView.findViewById(R.id.userAvatarIV)
            containerSV = itemView.findViewById(R.id.containerSV)

        }

    }
}