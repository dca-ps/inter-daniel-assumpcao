package br.com.danielassumpcao.inter.models
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(val total_count: Int, val items: List<Repository>): java.io.Serializable
