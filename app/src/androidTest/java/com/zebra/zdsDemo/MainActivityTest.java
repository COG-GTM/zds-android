package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.TestUtils.assertToolbarTitle;
import static com.zebra.zdsDemo.TestUtils.navigateTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void startsOnButtonsScreenWithToolbar() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));
        onView(withId(R.id.buttonList)).check(matches(isDisplayed()));
        onView(withId(R.id.materialButton)).check(matches(isDisplayed()));
    }

    @Test
    public void drawerOpensAndShowsHeaderAndFooter() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)));

        onView(allOf(withId(R.id.title), withText("Title"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), withText("Subtitle"))).check(matches(isDisplayed()));
        onView(withId(R.id.footer_subtitle)).check(matches(withText("ZDS Android v1.1.0")));

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));
    }

    @Test
    public void drawerNavigationSwitchesScreensAndUpdatesTitle() {
        navigateTo(R.id.switchFragment);
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));
        assertToolbarTitle("Switch");
        onView(withId(R.id.zebra_switch)).check(matches(isDisplayed()));

        navigateTo(R.id.checkboxFragment);
        assertToolbarTitle("Checkbox");
        onView(withId(R.id.optionLabel)).check(matches(isDisplayed()));
    }

    @Test
    public void systemBannerIsHiddenByDefault() {
        onView(withId(R.id.systemBanner)).check(matches(not(isDisplayed())));
    }
}
