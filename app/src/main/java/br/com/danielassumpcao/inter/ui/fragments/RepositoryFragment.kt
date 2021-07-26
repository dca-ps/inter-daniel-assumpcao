package br.com.danielassumpcao.inter.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.databinding.FragmentRepositoryBinding
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.ui.adapter.RepositoryAdapter
import br.com.danielassumpcao.inter.ui.contract.RepositoryContract
import br.com.danielassumpcao.inter.ui.listeners.RepositoryClickListener
import br.com.danielassumpcao.inter.ui.presenters.RepositoryPresenter
import com.google.android.material.snackbar.Snackbar


class RepositoryFragment : Fragment(), RepositoryContract.View, RepositoryClickListener {

    private val LIST_STATE_KEY = "LIST_STATE_KEY"

    private var _binding: FragmentRepositoryBinding? = null
    private lateinit var presenter: RepositoryContract.Presenter
    private lateinit var adapter: RepositoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    val repositoryDataSet: ArrayList<Repository> = ArrayList()
    private var isLoadingList = false

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RepositoryPresenter(this)


        val list = presenter.getSavedRepositories()

        list?.let {
            repositoryDataSet.clear()
            repositoryDataSet.addAll(it)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = RepositoryAdapter(repositoryDataSet, context, this)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.onRestoreInstanceState(savedInstanceState?.getParcelable(LIST_STATE_KEY))
        binding.repositoryRV.layoutManager = layoutManager
        binding.repositoryRV.adapter = adapter
        binding.repositoryRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                linearLayoutManager?.let{
                    if (dy > 0) { //check for scroll down
                        val visibleItemCount: Int = it.getChildCount();
                        val totalItemCount: Int  = it.getItemCount();
                        val pastVisiblesItems: Int  = it.findFirstVisibleItemPosition();

                        if (!isLoadingList) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                presenter.getRepositories()
                            }
                        }
                    }
                }
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            repositoryDataSet.clear()
            adapter.notifyDataSetChanged()
            presenter.getRepositories()
        }

        if(repositoryDataSet.isEmpty()){
            presenter.getRepositories()
        }
        else{
            stopLoading()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val recycleViewState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, recycleViewState);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.saveRepositories(repositoryDataSet)
        _binding = null
    }

    override fun getDataSetSize(): Int {
        return repositoryDataSet.size
    }

    override fun getSharedPreferences(): SharedPreferences {
        return requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun stopLoading() {
        isLoadingList = false
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressBar.visibility = View.INVISIBLE
        binding.repositoryRV.visibility = View.VISIBLE

    }

    override fun startLoading() {
        isLoadingList = true
        if (!binding.swipeRefreshLayout.isRefreshing) binding.progressBar.visibility = View.VISIBLE
    }

    override fun onRepositoriesFailure() {
        Snackbar.make(binding.repositoryRV, R.string.request_error, Snackbar.LENGTH_SHORT).show()
    }

    override fun onRepositoriesSuccess(repositories: List<Repository>) {
        repositoryDataSet.addAll(repositories)
        adapter.notifyDataSetChanged()
    }

    override fun onRepositoryClick(repo: Repository) {
        val action = RepositoryFragmentDirections.actionRepositoryFragmentToPullRequestFragment(repo)
        findNavController().navigate(action)
    }
}