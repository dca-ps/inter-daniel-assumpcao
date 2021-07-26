package br.com.danielassumpcao.inter.ui.contract

import android.content.SharedPreferences
import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.models.Repository

interface PullRequestContract {
    interface View {
        fun stopLoading()
        fun startLoading()
        fun getSharedPreferences(): SharedPreferences
        fun getStringFromResouces(id: Int, repo: Long): String
        fun onPullRequestsFailure()
        fun onPullRequestsSuccess(pullRequests: List<PullRequest>)
    }

    interface Presenter {
        fun getPullRequests(owner: String, repo: String)

        fun savePullRequests(repo: Long, pullRequests: List<PullRequest>)
        fun getSavedPullRequests(repo: Long) : List<PullRequest>?
    }
}