package br.com.danielassumpcao.inter.models
import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Long, val login: String, val avatar_url: String): java.io.Serializable
