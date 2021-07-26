package br.com.danielassumpcao.inter.ui.presenters

import android.content.Context
import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.models.SearchResult
import br.com.danielassumpcao.inter.services.GithubService
import br.com.danielassumpcao.inter.services.RetrofitConfig
import br.com.danielassumpcao.inter.ui.contract.RepositoryContract
import br.com.danielassumpcao.inter.ui.fragments.RepositoryFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryPresenter(private val view: RepositoryContract.View): RepositoryContract.Presenter {
    val PAGE_SIZE: Int = 30
    val gson = Gson()
    val sharedPref = view.getSharedPreferences()

    override fun saveRepositories(repositories: List<Repository>){
        with (sharedPref.edit()) {
            val jsonText = gson.toJson(repositories)
            putString(view.getString(R.string.repository_key), jsonText)
            apply()
        }
    }

    override fun getSavedRepositories() : List<Repository>?{
        val jsonText: String? = sharedPref.getString(view.getString(R.string.repository_key), null)
        val itemType = object : TypeToken<List<Repository>>() {}.type
        return gson.fromJson<List<Repository>>(jsonText, itemType)
    }


    override fun getRepositories() {
        val service: GithubService = RetrofitConfig.getGithubService()
        val page = (view.getDataSetSize()/PAGE_SIZE)+1

        //Api Limitation
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