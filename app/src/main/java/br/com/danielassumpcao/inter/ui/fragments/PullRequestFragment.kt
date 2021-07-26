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
import br.com.danielassumpcao.inter.R
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

    private var presenter: PullRequestContract.Presenter? = null
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
                presenter?.getPullRequests(it.owner.login, it.name)
            }

            val list = presenter?.getSavedPullRequests(it.id)

            list?.let { l ->
                if(l.isEmpty()) presenter?.getPullRequests(it.owner.login, it.name)
                else {
                    pullRequestDataSet.clear()
                    pullRequestDataSet.addAll(l)
                    stopLoading()
                }
            }?: run{
                presenter?.getPullRequests(it.owner.login, it.name)
            }

        }?: run{
            Snackbar.make(binding.pullRequestRV, R.string.request_error, Snackbar.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        selectedRepo?.let{
            presenter?.savePullRequests(it.id, pullRequestDataSet)
        }
        _binding = null
        presenter = null

    }

    override fun stopLoading() {
        if(_binding != null){
            binding.pullRequestRV.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
        }
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
        Snackbar.make(binding.pullRequestRV, R.string.request_error, Snackbar.LENGTH_SHORT).show()
    }

    fun checkEmptyPullRequest(pullRequests: List<PullRequest>){
        if(_binding != null) {
            if (pullRequests.isEmpty()) binding.emptyPullRequestTV.visibility = View.VISIBLE
            else binding.emptyPullRequestTV.visibility = View.GONE
        }
    }

    override fun onPullRequestsSuccess(pullRequests: List<PullRequest>) {
        checkEmptyPullRequest(pullRequests)
        pullRequestDataSet.addAll(pullRequests)
        adapter.notifyDataSetChanged()
    }

    override fun onPullRequestClick(pullRequestUrl: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(pullRequestUrl)
        startActivity(openURL)
    }
}