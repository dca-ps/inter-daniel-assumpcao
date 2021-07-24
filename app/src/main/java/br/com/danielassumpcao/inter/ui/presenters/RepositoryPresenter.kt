package br.com.danielassumpcao.inter.ui.presenters

import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.models.SearchResult
import br.com.danielassumpcao.inter.services.GithubService
import br.com.danielassumpcao.inter.services.RetrofitConfig
import br.com.danielassumpcao.inter.ui.contract.RepositoryContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryPresenter(private val view: RepositoryContract.View): RepositoryContract.Presenter {
    override fun getRepositories() {
        val service: GithubService = RetrofitConfig.getGithubService()

        val call = service.getRepositories("language:Java", "stars",1)


        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                response.body()?.let {
                    view.onRepositoriesSuccess(it.items, it.total_count!!)

                } ?: run {
                    view.stopLoading()
                    view.onRepositoriesFailure()
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                view.stopLoading()
                view.onRepositoriesFailure()
            }
        })
    }
}