package br.com.danielassumpcao.inter.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.danielassumpcao.inter.R
import br.com.danielassumpcao.inter.databinding.FragmentPullRequestBinding
import br.com.danielassumpcao.inter.models.PullRequest
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.ui.adapter.PullRequestAdapter
import br.com.danielassumpcao.inter.ui.adapter.RepositoryAdapter
import br.com.danielassumpcao.inter.ui.contract.PullRequestContract
import br.com.danielassumpcao.inter.ui.contract.RepositoryContract
import br.com.danielassumpcao.inter.ui.listeners.PullRequestClickListener
import br.com.danielassumpcao.inter.ui.presenters.PullRequestPresenter
import br.com.danielassumpcao.inter.ui.presenters.RepositoryPresenter
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PullRequestFragment : Fragment(), PullRequestContract.View, PullRequestClickListener {

    private var _binding: FragmentPullRequestBinding? = null
    val args: PullRequestFragmentArgs by navArgs()

    private lateinit var presenter: PullRequestContract.Presenter
    private lateinit var adapter: PullRequestAdapter
    val pullRequestDataSet: ArrayList<PullRequest> = ArrayList()



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPullRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedRepo: Repository? = args.selectedRepo

        selectedRepo?.let{
            presenter = PullRequestPresenter(this)


            adapter = PullRequestAdapter(pullRequestDataSet, context, this)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.pullRequestRV.layoutManager = layoutManager
            binding.pullRequestRV.adapter = adapter
            presenter.getPullRequests(it.owner.login, it.name)

        }?: run{
            Snackbar.make(binding.pullRequestRV, "Falhou", Snackbar.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun stopLoading() {
        binding.pullRequestRV.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
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