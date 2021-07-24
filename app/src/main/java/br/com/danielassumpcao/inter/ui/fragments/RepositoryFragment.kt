package br.com.danielassumpcao.inter.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.danielassumpcao.inter.R
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


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = RepositoryPresenter(this)


        adapter = RepositoryAdapter(repositoryDataSet)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.repositoryRV.layoutManager = layoutManager
        binding.repositoryRV.adapter = adapter


        presenter.getRepositories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun stopLoading() {
        TODO("Not yet implemented")
    }

    override fun onRepositoriesFailure() {
        Snackbar.make(binding.repositoryRV, "Falhou", Snackbar.LENGTH_SHORT).show()
    }

    override fun onRepositoriesSuccess(Repositories: List<Repository>, totalItens: Int) {
        repositoryDataSet.addAll(Repositories)
        adapter.notifyDataSetChanged()
    }
}