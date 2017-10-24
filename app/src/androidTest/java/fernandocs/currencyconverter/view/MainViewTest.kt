package fernandocs.currencyconverter.view

import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import fernandocs.currencyconverter.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainViewTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {

        SystemClock.sleep(2500)

        onView(withId(R.id.recyclerViewRates)).perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withRecyclerView(R.id.recyclerViewRates).atPosition(0)).perform(click())

        pressBack()

        onView(withId(R.id.recyclerViewRates)).perform(scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withRecyclerView(R.id.recyclerViewRates).atPosition(1)).perform(click())

        pressBack()

        onView(withId(R.id.recyclerViewRates)).perform(scrollToPosition<RecyclerView.ViewHolder>(2))
        onView(withRecyclerView(R.id.recyclerViewRates).atPosition(2)).perform(click())

        onView(withId(R.id.editTextValue)).perform(replaceText("2.0"))
        SystemClock.sleep(1000)
        onView(withId(R.id.editTextValue)).perform(replaceText("3.0"))
        SystemClock.sleep(1000)
        onView(withId(R.id.editTextValue)).perform(replaceText("4.0"))
        SystemClock.sleep(1000)
        onView(withId(R.id.editTextValue)).perform(replaceText("1.0"))
        SystemClock.sleep(1000)

        pressBack()
    }

    companion object {

        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {

            return RecyclerViewMatcher(recyclerViewId)
        }
    }
}