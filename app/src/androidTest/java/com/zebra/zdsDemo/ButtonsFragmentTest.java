package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the ZdsButton showcase, which is the start destination of the demo app. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ButtonsFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void primaryButtonsAreShownAndClickable() {
        onView(withId(R.id.materialButton2)).perform(scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(withText("Button")))
                .check(matches(isEnabled()))
                .perform(click());

        onView(withId(R.id.materialButton)).perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void iconButtonVariantsAreShown() {
        onView(withId(R.id.materialButtonIconLeft2)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.materialButtonIconRight)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void outlineAndTextButtonsAreShown() {
        onView(withId(R.id.materialButtonOutline2)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.materialButtonOutlineSubtle)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.materialButtonText2)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void disabledButtonsAreNotEnabled() {
        onView(withId(R.id.materialButtonDisabled2)).perform(scrollTo())
                .check(matches(not(isEnabled())));
        onView(withId(R.id.materialButtonDisabled)).perform(scrollTo())
                .check(matches(not(isEnabled())));
    }
}
