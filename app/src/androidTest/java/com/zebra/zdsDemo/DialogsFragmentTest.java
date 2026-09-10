package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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

/** UI tests for the ZdsDialog showcase: the dialog builder options and dismissal. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DialogsFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToDialogs() {
        navigateTo(activityRule.getScenario(), R.id.dialogsFragment);
    }

    @Test
    public void dialogWithTwoActionsShowsTitleContentAndBothActions() {
        onView(withId(R.id.twoActions)).perform(scrollTo(), click());
        onView(withId(R.id.showDialogButton)).perform(scrollTo(), click());

        onView(withId(com.zebra.zds.R.id.titleText)).check(matches(withText("Dialog Title")));
        onView(withText("Confirm")).check(matches(isDisplayed()));
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

    @Test
    public void dialogWithThreeActionsShowsTheNeutralAction() {
        onView(withId(R.id.threeActions)).perform(scrollTo(), click());
        onView(withId(R.id.showDialogButton)).perform(scrollTo(), click());

        onView(withText("Learn More")).check(matches(isDisplayed()));
    }

    @Test
    public void clickingAnActionDismissesTheDialog() {
        onView(withId(R.id.oneAction)).perform(scrollTo(), click());
        onView(withId(R.id.showDialogButton)).perform(scrollTo(), click());

        onView(withText("Confirm")).check(matches(isDisplayed())).perform(click());

        onView(withId(com.zebra.zds.R.id.titleText)).check(doesNotExist());
    }
}
