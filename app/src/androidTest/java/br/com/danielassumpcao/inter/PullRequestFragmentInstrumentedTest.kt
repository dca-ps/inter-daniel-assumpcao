package br.com.danielassumpcao.inter

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.danielassumpcao.inter.models.Repository
import br.com.danielassumpcao.inter.models.User
import br.com.danielassumpcao.inter.ui.fragments.PullRequestFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PullRequestFragmentInstrumentedTest {

    @Test
    fun viewsDisplayAsExpected() {

        val repo = Repository(1234, "teste/teste", "teste", "Teste1234", 1234, 1234, User(1234,"teste", "google.com"))

        val fragmentArgs = bundleOf("selectedRepo" to repo)

        val scenario = launchFragmentInContainer<PullRequestFragment>(fragmentArgs)
        Espresso.onView(ViewMatchers.withId(R.id.pullRequestRV)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.swipeRefreshLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.progressBar)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

    }
}