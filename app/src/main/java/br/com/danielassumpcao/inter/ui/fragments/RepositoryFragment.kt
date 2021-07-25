package br.com.danielassumpcao.inter.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.danielassumpcao.inter.databinding.FragmentRepositoryBinding
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.ui.adapter.RepositoryAdapter
import br.com.danielassumpcao.inter.ui.contract.RepositoryContract
import br.com.danielassumpcao.inter.ui.presenters.RepositoryPresenter
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RepositoryFragment : Fragment(), RepositoryContract.View {

    private var _binding: FragmentRepositoryBinding? = null
    private lateinit var presenter: RepositoryContract.Presenter
    private lateinit var adapter: RepositoryAdapter
    val repositoryDataSet: ArrayList<Repository> = ArrayList()
    private var isLoadingList = false

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = RepositoryPresenter(this)


        adapter = RepositoryAdapter(repositoryDataSet, context)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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


        presenter.getRepositories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getDataSetSize(): Int {
        return repositoryDataSet.size
    }



    override fun stopLoading() {
        isLoadingList = false
        binding.progressBar.visibility = View.INVISIBLE
        binding.repositoryRV.visibility = View.VISIBLE

    }

    override fun startLoading() {
        isLoadingList = true
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onRepositoriesFailure() {
        Snackbar.make(binding.repositoryRV, "Falhou", Snackbar.LENGTH_SHORT).show()
    }

    override fun onRepositoriesSuccess(repositories: List<Repository>) {
        repositoryDataSet.addAll(repositories)
        adapter.notifyDataSetChanged()
    }
}