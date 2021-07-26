package br.com.danielassumpcao.inter.presenters

import br.com.danielassumpcao.inter.ui.contract.PullRequestContract
import br.com.danielassumpcao.inter.ui.presenters.PullRequestPresenter
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PullRequestPresenterTest {
    @Mock
    private lateinit var view: PullRequestContract.View

    private lateinit var presenter: PullRequestPresenter

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this);
        presenter = PullRequestPresenter(view)
    }


    @Test
    fun getMoviesTestSuccess(){
        presenter.getPullRequests("CyC2018", "CS-Notes")

        verify(view, timeout(10000)).stopLoading()
        verify(view, timeout(10000)).onPullRequestsSuccess(ArgumentMatchers.anyList())
    }


    @Test
    fun getMoviesTestFail(){
        presenter.getPullRequests("-----", "----")

        verify(view, timeout(10000)).stopLoading()
        verify(view, timeout(10000)).onPullRequestsFailure()
    }




}