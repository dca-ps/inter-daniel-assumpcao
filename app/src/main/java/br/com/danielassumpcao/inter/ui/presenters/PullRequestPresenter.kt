package br.com.danielassumpcao.inter.ui.presenters

import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.models.SearchResult
import br.com.danielassumpcao.inter.services.GithubService
import br.com.danielassumpcao.inter.services.RetrofitConfig
import br.com.danielassumpcao.inter.ui.contract.PullRequestContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PullRequestPresenter(private val view: PullRequestContract.View): PullRequestContract.Presenter {
    override fun getPullRequests(owner: String, repo: String) {
        val service: GithubService = RetrofitConfig.getGithubService()

        val call = service.getPullRequests(owner, repo)

        view.startLoading()
        call.enqueue(object : Callback<ArrayList<PullRequest>> {
            override fun onResponse(call: Call<ArrayList<PullRequest>>, response: Response<ArrayList<PullRequest>>) {
                view.stopLoading()
                response.body()?.let {
                    view.onPullRequestsSuccess(it)
                } ?: run {
                    view.onPullRequestsFailure()
                }
            }

            override fun onFailure(call: Call<ArrayList<PullRequest>>, t: Throwable) {
                view.stopLoading()
                view.onPullRequestsFailure()            }
        })
    }

}