package br.com.danielassumpcao.inter.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.danielassumpcao.inter.databinding.FragmentPullRequestBinding
import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.ui.adapter.PullRequestAdapter
import br.com.danielassumpcao.inter.ui.contract.PullRequestContract
import br.com.danielassumpcao.inter.ui.listeners.PullRequestClickListener
import br.com.danielassumpcao.inter.ui.presenters.PullRequestPresenter
import com.google.android.material.snackbar.Snackbar

class PullRequestFragment : Fragment(), PullRequestContract.View, PullRequestClickListener {

    private var _binding: FragmentPullRequestBinding? = null
    val args: PullRequestFragmentArgs by navArgs()

    private lateinit var presenter: PullRequestContract.Presenter
    private lateinit var adapter: PullRequestAdapter
    private var selectedRepo: Repository? = null
    val pullRequestDataSet: ArrayList<PullRequest> = ArrayList()

    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPullRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedRepo = args.selectedRepo

        selectedRepo?.let{
            presenter = PullRequestPresenter(this)

            adapter = PullRequestAdapter(pullRequestDataSet, context, this)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.pullRequestRV.layoutManager = layoutManager
            binding.pullRequestRV.adapter = adapter

            binding.swipeRefreshLayout.setOnRefreshListener {
                pullRequestDataSet.clear()
                adapter.notifyDataSetChanged()
                presenter.getPullRequests(it.owner.login, it.name)
            }

            val list = presenter.getSavedPullRequests(it.id)

            list?.let {
                pullRequestDataSet.clear()
                pullRequestDataSet.addAll(it)
                stopLoading()
            }?: run{
                presenter.getPullRequests(it.owner.login, it.name)
            }

        }?: run{
            Snackbar.make(binding.pullRequestRV, "Falhou", Snackbar.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        selectedRepo?.let{
            presenter.savePullRequests(it.id, pullRequestDataSet)
        }
    }

    override fun stopLoading() {
        binding.pullRequestRV.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false

    }

    override fun startLoading() {
        if (!binding.swipeRefreshLayout.isRefreshing) binding.progressBar.visibility = View.VISIBLE
    }

    override fun getSharedPreferences(): SharedPreferences {
        return requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun getStringFromResouces(id: Int, repo: Long): String {
        return getString(id, repo)
    }

    override fun onPullRequestsFailure() {
        Snackbar.make(binding.pullRequestRV, "Falhou", Snackbar.LENGTH_SHORT).show()
    }

    override fun onPullRequestsSuccess(pullRequests: List<PullRequest>) {
        pullRequestDataSet.addAll(pullRequests)
        adapter.notifyDataSetChanged()
    }

    override fun onPullRequestClick(pullRequestUrl: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(pullRequestUrl)
        startActivity(openURL)
    }
}