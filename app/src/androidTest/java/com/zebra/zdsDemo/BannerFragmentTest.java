package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.ZdsTestSupport.navigateTo;
import static com.zebra.zdsDemo.ZdsTestSupport.withCheckedState;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the ZdsBanner and ZdsSystemBanner showcase. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class BannerFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToBanner() {
        navigateTo(activityRule.getScenario(), R.id.bannerFragment);
    }

    @Test
    public void bannerIsHiddenUntilShowBannerIsClicked() {
        onView(allOf(withId(com.zebra.zds.R.id.title), isDescendantOfA(withId(R.id.banner))))
                .check(matches(org.hamcrest.Matchers.not(isDisplayed())));

        onView(withId(R.id.showBanner)).perform(scrollTo(), click());

        onView(allOf(withId(com.zebra.zds.R.id.title), isDescendantOfA(withId(R.id.banner))))
                .check(matches(isDisplayed()))
                .check(matches(withText("Banner title")));
        onView(allOf(withId(com.zebra.zds.R.id.actionOne), isDescendantOfA(withId(R.id.banner))))
                .check(matches(withText("Action 1")));
    }

    @Test
    public void bannerStyleAndSharpToggleCanBeChanged() {
        onView(withId(R.id.info)).perform(scrollTo(), click())
                .check(matches(withCheckedState(true)));
        onView(withId(R.id.sharpStyle)).perform(scrollTo(), click())
                .check(matches(withCheckedState(true)));

        onView(withId(R.id.showBanner)).perform(scrollTo(), click());

        onView(allOf(withId(com.zebra.zds.R.id.message), isDescendantOfA(withId(R.id.banner))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void systemBannerIsShownFromTheFragmentControls() {
        onView(withId(R.id.showSystemBanner)).perform(scrollTo(), click());

        onView(allOf(withId(com.zebra.zds.R.id.title), isDescendantOfA(withId(R.id.systemBanner))))
                .check(matches(isDisplayed()))
                .check(matches(withText("Banner title")));
    }
}
