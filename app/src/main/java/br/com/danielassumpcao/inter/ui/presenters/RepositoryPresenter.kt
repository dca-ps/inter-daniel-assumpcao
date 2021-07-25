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
    val PAGE_SIZE: Int = 30

    override fun getRepositories() {
        val service: GithubService = RetrofitConfig.getGithubService()
        val page = (view.getDataSetSize()/PAGE_SIZE)+1

        if (page > 34) return

        val call = service.getRepositories("language:Java", "stars",page)

        view.startLoading()
        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                view.stopLoading()

                response.body()?.let {
                    view.onRepositoriesSuccess(it.items)
                } ?: run {
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