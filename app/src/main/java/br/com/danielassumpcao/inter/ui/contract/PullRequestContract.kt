package br.com.danielassumpcao.inter.ui.contract

import br.com.danielassumpcao.inter.models.PullRequest

interface PullRequestContract {
    interface View {
        fun stopLoading()
        fun startLoading()
        fun onPullRequestsFailure()
        fun onPullRequestsSuccess(pullRequests: List<PullRequest>)
    }

    interface Presenter {
        fun getPullRequests(owner: String, repo: String)
    }
}