package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.ZdsTestSupport.navigateTo;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the snackbar showcase. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SnackbarFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToSnackbar() {
        navigateTo(activityRule.getScenario(), R.id.snackbarFragment);
    }

    @Test
    public void snackbarWithActionIsShownOnClick() {
        onView(withId(R.id.show_snackbar)).perform(scrollTo(), click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("This is a snackbar")))
                .check(matches(isDisplayed()));
        onView(withId(com.google.android.material.R.id.snackbar_action))
                .check(matches(withText("Action")));
    }
}
