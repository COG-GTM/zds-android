package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.ZdsTestSupport.navigateTo;
import static com.zebra.zdsDemo.ZdsTestSupport.withCheckedState;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the ZdsCheckBox showcase. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckBoxFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToCheckBoxes() {
        navigateTo(activityRule.getScenario(), R.id.checkboxFragment);
    }

    @Test
    public void checkBoxTogglesOnAndOff() {
        onView(withId(R.id.option2)).perform(scrollTo())
                .check(matches(withCheckedState(false)))
                .perform(click())
                .check(matches(withCheckedState(true)))
                .perform(click())
                .check(matches(withCheckedState(false)));
    }

    @Test
    public void labelledCheckBoxKeepsItsLabelWhenChecked() {
        onView(withId(R.id.optionLabel)).perform(scrollTo())
                .check(matches(withText("Option label")))
                .perform(click())
                .check(matches(withCheckedState(true)))
                .check(matches(withText("Option label")));
    }

    @Test
    public void disabledCheckBoxStaysChecked() {
        onView(withId(R.id.optionLabelDisabled)).perform(scrollTo())
                .check(matches(not(isEnabled())))
                .check(matches(withCheckedState(true)));
    }
}
