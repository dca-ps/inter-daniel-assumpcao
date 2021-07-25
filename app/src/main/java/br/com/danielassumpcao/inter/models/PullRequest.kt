package br.com.danielassumpcao.inter.models

import kotlinx.serialization.Serializable

@Serializable
data class PullRequest(val id: Long, val title: String, val body: String, val html_url: String, val created_at: String, val user: User): java.io.Serializable
