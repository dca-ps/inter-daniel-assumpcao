package br.com.danielassumpcao.inter

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.danielassumpcao.inter.ui.fragments.RepositoryFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RepositoryFragmentInstrumentedTest {


    @Test
    fun viewsDisplayAsExpected() {
        val scenario = launchFragmentInContainer<RepositoryFragment>()
        onView(withId(R.id.repositoryRV)).check(matches(not(isDisplayed())))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))

    }


}