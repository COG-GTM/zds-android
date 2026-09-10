package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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

/** UI tests for the ZdsRadioButton showcase. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class RadioFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToRadioButtons() {
        navigateTo(activityRule.getScenario(), R.id.radioFragment);
    }

    @Test
    public void selectingARadioButtonDeselectsTheOthersInItsGroup() {
        onView(withId(R.id.option1)).perform(scrollTo(), click())
                .check(matches(withCheckedState(true)));

        onView(withId(R.id.option2)).perform(scrollTo(), click())
                .check(matches(withCheckedState(true)));
        onView(withId(R.id.option1)).check(matches(withCheckedState(false)));
    }

    @Test
    public void selectingALabelledRadioButtonClearsThePreselectedOne() {
        onView(withId(R.id.option4Label)).perform(scrollTo())
                .check(matches(withCheckedState(true)));

        onView(withId(R.id.option1Label)).perform(scrollTo(), click())
                .check(matches(withCheckedState(true)));

        onView(withId(R.id.option4Label)).check(matches(withCheckedState(false)));
        onView(withId(R.id.option2)).check(matches(withCheckedState(false)));
    }

    @Test
    public void disabledRadioButtonsKeepTheirInitialState() {
        onView(withId(R.id.option3)).perform(scrollTo())
                .check(matches(not(isEnabled())))
                .check(matches(withCheckedState(false)));
        onView(withId(R.id.option4)).perform(scrollTo())
                .check(matches(not(isEnabled())))
                .check(matches(withCheckedState(true)));
    }
}
