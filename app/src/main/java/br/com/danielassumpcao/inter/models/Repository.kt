package br.com.danielassumpcao.inter.models

data class Repository(val id: Long?, val full_name: String?, val name: String?, val description: String?, val stargazers_count: Long?, val forks_count: Long?, val owner: Owner)
