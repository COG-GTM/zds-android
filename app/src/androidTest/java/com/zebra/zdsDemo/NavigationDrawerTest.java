package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the navigation drawer that hosts every component demo. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openDrawerAndSelect(String itemTitle) {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed())).perform(open());
        onView(allOf(withText(itemTitle), isDescendantOfA(withId(R.id.navigation_view)), isDisplayed()))
                .perform(click());
    }

    @Test
    public void drawerOpensAndShowsTheHeader() {
        onView(withId(R.id.drawer_layout)).perform(open()).check(matches(isOpen()));

        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.navigation_view))))
                .check(matches(withText("Title")));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.navigation_view))))
                .check(matches(withText("Subtitle")));
    }

    @Test
    public void selectingSwitchNavigatesToTheSwitchDemoAndClosesTheDrawer() {
        openDrawerAndSelect("Switch");

        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(withId(R.id.zebra_switch)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingDialogsNavigatesToTheDialogDemo() {
        openDrawerAndSelect("Dialogs");

        onView(withId(R.id.showDialogButton)).check(matches(isDisplayed()));
    }
}
