package br.com.danielassumpcao.inter.ui.contract

import br.com.danielassumpcao.inter.models.Repository

interface RepositoryContract {

    interface View {
        fun stopLoading()
        fun onRepositoriesFailure()
        fun onRepositoriesSuccess(Repositories: List<Repository>, totalItens: Int)
    }

    interface Presenter {
        fun getRepositories()
    }
}