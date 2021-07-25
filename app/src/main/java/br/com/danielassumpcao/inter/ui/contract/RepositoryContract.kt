package br.com.danielassumpcao.inter.ui.contract

import br.com.danielassumpcao.inter.models.Repository

interface RepositoryContract {

    interface View {
        fun getDataSetSize():Int
        fun stopLoading()
        fun startLoading()

        fun onRepositoriesFailure()
        fun onRepositoriesSuccess(repositories: List<Repository>)
    }

    interface Presenter {
        fun getRepositories()
    }
}