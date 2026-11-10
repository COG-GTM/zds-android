package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.test.espresso.matcher.ViewMatchers.Visibility;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    private static final int SEARCH_TEXT = androidx.appcompat.R.id.search_src_text;
    private static final int CLOSE_BUTTON = androidx.appcompat.R.id.search_close_btn;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void openSearchScreen() {
        activityRule.getScenario().onActivity(activity ->
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.searchFragment));
    }

    @Test
    public void clearButtonOnlyShownWithQuery() {
        onView(withId(CLOSE_BUTTON)).check(matches(withEffectiveVisibility(Visibility.GONE)));

        onView(withId(SEARCH_TEXT)).perform(click(), typeText("tab"));
        onView(withId(CLOSE_BUTTON)).check(matches(isDisplayed()));
    }

    @Test
    public void clearButtonClearsQueryAndKeepsFocus() {
        onView(withId(SEARCH_TEXT)).perform(click(), typeText("tab"));
        onView(withId(CLOSE_BUTTON)).perform(click());

        onView(withId(SEARCH_TEXT)).check(matches(withText(""))).check(matches(hasFocus()));
        onView(withId(CLOSE_BUTTON)).check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

    @Test
    public void queryFiltersComponentList() {
        onView(withId(SEARCH_TEXT)).perform(click(), typeText("button"));

        onView(resultWithText("Buttons")).check(matches(isDisplayed()));
        onView(resultWithText("Icon Buttons")).check(matches(isDisplayed()));
        onView(resultWithText("Snackbar")).check(doesNotExist());
        onView(withId(R.id.search_empty_state)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nonMatchingQueryShowsEmptyState() {
        onView(withId(SEARCH_TEXT)).perform(click(), typeText("zzz"), pressImeActionButton());

        onView(withId(R.id.search_empty_state))
                .check(matches(isDisplayed()))
                .check(matches(withText("No results for \u201Czzz\u201D")));
        onView(withId(SEARCH_TEXT)).check(matches(not(hasFocus())));
    }

    private static Matcher<View> resultWithText(String text) {
        return allOf(withText(text), isDescendantOfA(withId(R.id.search_results)));
    }
}
