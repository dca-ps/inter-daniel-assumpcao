package br.com.danielassumpcao.inter.services

import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.models.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/repositories")
    fun getRepositories(@Query("q") query: String, @Query("sort") sort: String, @Query("page") page: Int): Call<SearchResult>


    @GET("/repos/{owner}/{repo}/pulls")
    fun getPullRequests(@Path("owner") owner: String, @Path("repo") repo: String): Call<ArrayList<PullRequest>>


}