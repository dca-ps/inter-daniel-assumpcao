package br.com.danielassumpcao.inter.ui.contract

import android.content.SharedPreferences
import br.com.danielassumpcao.inter.models.Repository

interface RepositoryContract {

    interface View {
        fun getDataSetSize():Int
        fun getSharedPreferences(): SharedPreferences
        fun getString(id: Int): String
        fun stopLoading()
        fun startLoading()

        fun onRepositoriesFailure()
        fun onRepositoriesSuccess(repositories: List<Repository>)
    }

    interface Presenter {
        fun getRepositories()
        fun saveRepositories(repositories: List<Repository>)
        fun getSavedRepositories() : List<Repository>?
    }
}