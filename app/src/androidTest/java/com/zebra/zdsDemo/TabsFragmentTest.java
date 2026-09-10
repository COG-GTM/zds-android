package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.ZdsTestSupport.navigateTo;
import static com.zebra.zdsDemo.ZdsTestSupport.withSelectedTab;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the ZdsTabLayout showcase. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TabsFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToTabs() {
        navigateTo(activityRule.getScenario(), R.id.tabsFragment);
    }

    @Test
    public void firstTabIsSelectedByDefault() {
        onView(withId(R.id.tabLayout))
                .check(matches(isDisplayed()))
                .check(matches(withSelectedTab(0)));
    }

    @Test
    public void tappingATabSelectsIt() {
        onView(allOf(withText("Tab3"), isDescendantOfA(withId(R.id.tabLayout)))).perform(click());

        onView(withId(R.id.tabLayout)).check(matches(withSelectedTab(2)));
    }

    @Test
    public void scrollableTabLayoutSelectionIsIndependent() {
        onView(allOf(withText("Tab2"), isDescendantOfA(withId(R.id.tabLayoutContinuous)))).perform(click());

        onView(withId(R.id.tabLayoutContinuous)).check(matches(withSelectedTab(1)));
        onView(withId(R.id.tabLayout)).check(matches(withSelectedTab(0)));
    }
}
