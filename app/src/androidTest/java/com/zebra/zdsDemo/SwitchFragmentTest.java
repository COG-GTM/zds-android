package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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

/** UI tests for the ZdsSwitch showcase. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SwitchFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToSwitches() {
        navigateTo(activityRule.getScenario(), R.id.switchFragment);
    }

    @Test
    public void switchTogglesOnAndOff() {
        onView(withId(R.id.zebra_switch)).perform(scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(withCheckedState(false)))
                .perform(click())
                .check(matches(withCheckedState(true)))
                .perform(click())
                .check(matches(withCheckedState(false)));
    }

    @Test
    public void disabledSwitchStaysOff() {
        onView(withId(R.id.zebra_switch_disabled)).perform(scrollTo())
                .check(matches(not(isEnabled())))
                .check(matches(withCheckedState(false)));
    }
}
