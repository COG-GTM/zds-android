package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.ZdsTestSupport.navigateTo;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the ZdsTextInput showcase: text entry plus the error state toggles. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TextInputFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToTextInput() {
        navigateTo(activityRule.getScenario(), R.id.textInputFragment);
    }

    private static Matcher<View> editTextOf(int zdsTextInputId) {
        return allOf(withId(com.zebra.zds.R.id.textInputEditText), isDescendantOfA(withId(zdsTextInputId)));
    }

    private static Matcher<View> errorTextOf(int zdsTextInputId) {
        return allOf(withId(com.zebra.zds.R.id.textError), isDescendantOfA(withId(zdsTextInputId)));
    }

    @Test
    public void typedTextIsShownInTheInput() {
        onView(editTextOf(R.id.zebra_text_input)).perform(scrollTo(), replaceText("Zebra"));
        closeSoftKeyboard();

        onView(editTextOf(R.id.zebra_text_input)).check(matches(withText("Zebra")));
    }

    @Test
    public void labelsAreRenderedWithTheirStyleSuffix() {
        onView(allOf(withId(com.zebra.zds.R.id.textLabel), isDescendantOfA(withId(R.id.zebra_text_input))))
                .perform(scrollTo())
                .check(matches(withText("Custom label*")));

        onView(allOf(withId(com.zebra.zds.R.id.textLabel), isDescendantOfA(withId(R.id.zebra_text_input_dense))))
                .perform(scrollTo())
                .check(matches(withText("Optional label (Optional)")));
    }

    @Test
    public void setErrorShowsErrorTextAndClearErrorRestoresTheHint() {
        onView(withId(R.id.buttonSetError)).perform(scrollTo(), click());

        onView(errorTextOf(R.id.zebra_text_input)).perform(scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(withText("Error")));

        onView(withId(R.id.buttonClearError)).perform(scrollTo(), click());

        onView(errorTextOf(R.id.zebra_text_input)).perform(scrollTo())
                .check(matches(withText("Hint")));
    }
}
