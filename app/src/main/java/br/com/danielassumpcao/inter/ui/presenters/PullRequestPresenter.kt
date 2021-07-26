package br.com.danielassumpcao.inter.ui.presenters

import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.services.GithubService
import br.com.danielassumpcao.inter.services.RetrofitConfig
import br.com.danielassumpcao.inter.ui.contract.PullRequestContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PullRequestPresenter(private val view: PullRequestContract.View): PullRequestContract.Presenter {
    val gson = Gson()
    val sharedPref = view.getSharedPreferences()

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

    override fun savePullRequests(repo: Long, pullRequests: List<PullRequest>) {
        with (sharedPref.edit()) {
            val jsonText = gson.toJson(pullRequests)
            putString(view.getStringFromResouces(R.string.pull_request_key, repo), jsonText)
            apply()
        }
    }

    override fun getSavedPullRequests(repo: Long): List<PullRequest>? {
        val jsonText: String? = sharedPref.getString(view.getStringFromResouces(R.string.pull_request_key, repo), null)
        val itemType = object : TypeToken<List<PullRequest>>() {}.type
        return gson.fromJson<List<PullRequest>>(jsonText, itemType)
    }

}