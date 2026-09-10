package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
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

/** UI tests for the ZdsListItem showcase backed by a RecyclerView. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ListFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToList() {
        navigateTo(activityRule.getScenario(), R.id.listFragment);
    }

    @Test
    public void listRendersItemsAndScrollsToLaterOnes() {
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        onView(withText("List Item 0")).check(matches(isDisplayed()));

        onView(withId(R.id.list)).perform(scrollToPosition(15));

        onView(withId(R.id.list)).check(matches(hasDescendant(withText("List Item 15"))));
        onView(withId(R.id.list)).check(matches(hasDescendant(withText("Descriptor 15"))));
    }

    @Test
    public void metaOptionAddsMetaTextToEveryItem() {
        onView(withId(R.id.showMeta)).perform(click()).check(matches(withCheckedState(true)));

        onView(withId(R.id.list)).perform(scrollToPosition(0));
        onView(withId(R.id.list))
                .check(matches(hasDescendant(
                        allOf(withId(com.zebra.zds.R.id.meta), withText("Meta")))));
    }

    @Test
    public void dividerOptionKeepsTheListRendered() {
        onView(withId(R.id.dividers)).perform(click()).check(matches(withCheckedState(true)));

        onView(withId(R.id.list)).perform(scrollToPosition(0));
        onView(withText("List Item 0")).check(matches(isDisplayed()));
    }
}
