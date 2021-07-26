package br.com.danielassumpcao.inter.presenters

import br.com.danielassumpcao.inter.ui.contract.RepositoryContract
import br.com.danielassumpcao.inter.ui.presenters.RepositoryPresenter
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RepositoryPresenterTest {



    @Mock
    private lateinit var view: RepositoryContract.View

    private lateinit var presenter: RepositoryPresenter

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this);
        presenter = RepositoryPresenter(view)
    }


    @Test
    fun getMoviesTestSuccess(){
        presenter.getRepositories()

        verify(view, timeout(10000)).stopLoading()
        verify(view, timeout(10000)).onRepositoriesSuccess(ArgumentMatchers.anyList())
    }
}