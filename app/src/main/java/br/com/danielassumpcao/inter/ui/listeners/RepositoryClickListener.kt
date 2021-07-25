package br.com.danielassumpcao.inter.ui.listeners

import br.com.danielassumpcao.inter.models.Repository

interface RepositoryClickListener {
    fun onRepositoryClick(repo: Repository)
}