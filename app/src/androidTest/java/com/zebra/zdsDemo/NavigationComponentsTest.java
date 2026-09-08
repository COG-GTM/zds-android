package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.TestUtils.navigateTo;
import static com.zebra.zdsDemo.TestUtils.onScrolledView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for navigation/collection components: tabs, lists, dropdowns, search, toolbar. */
@RunWith(AndroidJUnit4.class)
public class NavigationComponentsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // ---- Tabs ----------------------------------------------------------------------------------

    @Test
    public void tabs_selectingTabUpdatesSelection() {
        navigateTo(R.id.tabsFragment);

        onView(allOf(withText("Tab1"), isDescendantOfA(withId(R.id.tabLayout)))).check(matches(isDisplayed()));
        onView(allOf(withText("Tab3"), isDescendantOfA(withId(R.id.tabLayout)))).perform(click());
        onView(allOf(withText("Tab3"), isDescendantOfA(withId(R.id.tabLayout)))).check(matches(isSelected()));
        onView(allOf(withText("Tab1"), isDescendantOfA(withId(R.id.tabLayout)))).check(matches(not(isSelected())));
    }

    @Test
    public void tabs_scrollableLayoutRendersAllTabs() {
        navigateTo(R.id.tabsFragment);

        onView(withId(R.id.tabLayoutContinuous)).check(matches(hasDescendant(withText("Tab1"))));
        onView(withId(R.id.tabLayoutContinuous)).check(matches(hasDescendant(withText("Tab6"))));
        onView(allOf(withText("Tab2"), isDescendantOfA(withId(R.id.tabLayoutContinuous)))).perform(click());
        onView(allOf(withText("Tab2"), isDescendantOfA(withId(R.id.tabLayoutContinuous)))).check(matches(isSelected()));
    }

    // ---- Lists ---------------------------------------------------------------------------------

    @Test
    public void list_rendersItemsAndScrollsToEnd() {
        navigateTo(R.id.listFragment);

        onView(withId(R.id.list)).check(matches(hasDescendant(withText("List Item 0"))));
        onView(withId(R.id.list)).check(matches(hasDescendant(withText("Descriptor 0"))));

        onScrolledView(withText("List Item 15")).check(matches(isDisplayed()));
        onView(withText("List Item 16")).check(doesNotExist());
    }

    @Test
    public void list_togglingControlsChangesItemDecorations() {
        navigateTo(R.id.listFragment);

        onView(withId(R.id.showMeta)).perform(click());
        onView(withId(R.id.showMeta)).check(matches(isChecked()));
        onView(allOf(withId(R.id.meta), hasSiblingText("List Item 0"))).check(matches(withText("Meta")));

        onView(withId(R.id.showActionView)).perform(click());
        onView(allOf(withId(R.id.action), hasSiblingText("List Item 0"))).check(matches(isDisplayed()));

        onView(withId(R.id.dividers)).perform(click());
        onView(withId(R.id.dividers)).check(matches(isChecked()));
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    private static Matcher<View> hasSiblingText(String text) {
        return hasSibling(hasDescendant(withText(text)));
    }

    // ---- Dropdown ------------------------------------------------------------------------------

    @Test
    public void dropdown_defaultMenuShowsOptionsAndClosesOnSelect() {
        navigateTo(R.id.dropdownFragment);

        onScrolledView(withId(R.id.defaultButton)).perform(click());
        onView(withText("Option 1")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(withText("Option 4")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));

        onView(withText("Option 2")).inRoot(isPlatformPopup()).perform(click());
        onView(withText("Option 2")).check(doesNotExist());
    }

    @Test
    public void dropdown_checkboxMenuShowsCheckableOptions() {
        navigateTo(R.id.dropdownFragment);

        onScrolledView(withId(R.id.checkboxButton)).perform(click());
        onView(withText("Option 3")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        pressBack();
        onView(withText("Option 3")).check(doesNotExist());
    }

    // ---- Search --------------------------------------------------------------------------------

    @Test
    public void search_acceptsQueryText() {
        navigateTo(R.id.searchFragment);

        onView(withId(R.id.zebra_search)).check(matches(isDisplayed()));
        onView(withId(R.id.zebra_search)).perform(click());
        onView(allOf(withId(androidx.appcompat.R.id.search_src_text), isDescendantOfA(withId(R.id.zebra_search))))
                .perform(typeText("zebra"), closeSoftKeyboard())
                .check(matches(withText("zebra")));
    }

    // ---- Toolbar -------------------------------------------------------------------------------

    @Test
    public void toolbar_showsFragmentMenuAndContextualActionMode() {
        navigateTo(R.id.toolbarFragment);

        onView(withId(R.id.action_delete)).check(matches(isDisplayed()));
        onScrolledView(withId(R.id.toolbar_avatar)).check(matches(isDisplayed()));
        onScrolledView(withId(R.id.toolbar_search)).check(matches(isDisplayed()));

        onScrolledView(withId(R.id.start_contextual_mode)).perform(click());
        onView(withId(R.id.action_test)).check(matches(isDisplayed()));
        onView(withContentDescription("Done")).perform(click());
        onView(withId(R.id.action_test)).check(doesNotExist());
    }
}
