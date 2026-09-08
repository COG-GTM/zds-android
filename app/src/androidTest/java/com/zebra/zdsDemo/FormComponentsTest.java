package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.TestUtils.assertEventually;
import static com.zebra.zdsDemo.TestUtils.navigateTo;
import static com.zebra.zdsDemo.TestUtils.onScrolledView;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for the form-style ZDS components: buttons, switch, radio, checkbox, text/select input. */
@RunWith(AndroidJUnit4.class)
public class FormComponentsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private static Matcher<View> helperTextOf(int inputId) {
        return allOf(withId(R.id.textError), isDescendantOfA(withId(inputId)));
    }

    // ---- Buttons -------------------------------------------------------------------------------

    @Test
    public void buttons_enabledAndDisabledVariantsAreRendered() {
        onScrolledView(withId(R.id.materialButton)).check(matches(allOf(isDisplayed(), isEnabled())));
        onScrolledView(withId(R.id.materialButton2)).check(matches(allOf(isDisplayed(), isEnabled())));
        onScrolledView(withId(R.id.materialButtonDisabled)).check(matches(allOf(isDisplayed(), isNotEnabled())));
    }

    @Test
    public void iconButtons_screenRendersEnabledAndDisabledVariants() {
        navigateTo(R.id.iconButtonsFragment);
        onScrolledView(withId(R.id.materialButton)).check(matches(allOf(isDisplayed(), isEnabled())));
        onScrolledView(withId(R.id.materialButtonDisabled)).check(matches(allOf(isDisplayed(), isNotEnabled())));
    }

    @Test
    public void fab_screenRendersRegularExtendedAndDisabledVariants() {
        navigateTo(R.id.FABFragment);
        onScrolledView(withId(R.id.materialButton)).check(matches(allOf(isDisplayed(), isEnabled())));
        onScrolledView(withId(R.id.materialButtonDisabled)).check(matches(allOf(isDisplayed(), isNotEnabled())));
        onScrolledView(withId(R.id.materialButtonExtended2))
                .check(matches(allOf(isDisplayed(), isEnabled(), withText("Label"))));
    }

    // ---- Switch --------------------------------------------------------------------------------

    @Test
    public void switch_togglesOnClickAndDisabledStaysOff() {
        navigateTo(R.id.switchFragment);

        onView(withId(R.id.zebra_switch)).check(matches(allOf(isDisplayed(), isEnabled(), isNotChecked())));
        onView(withId(R.id.zebra_switch)).perform(click());
        onView(withId(R.id.zebra_switch)).check(matches(isChecked()));
        onView(withId(R.id.zebra_switch)).perform(click());
        onView(withId(R.id.zebra_switch)).check(matches(isNotChecked()));

        onView(withId(R.id.zebra_switch_disabled)).check(matches(allOf(isDisplayed(), isNotEnabled())));
    }

    // ---- Radio buttons -------------------------------------------------------------------------

    @Test
    public void radio_selectionIsExclusiveWithinGroup() {
        navigateTo(R.id.radioFragment);

        onView(withId(R.id.option3)).check(matches(allOf(isNotEnabled(), isNotChecked())));
        onView(withId(R.id.option4)).check(matches(allOf(isNotEnabled(), isChecked())));

        onView(withId(R.id.option1)).perform(click());
        onView(withId(R.id.option1)).check(matches(isChecked()));
        onView(withId(R.id.option2)).check(matches(isNotChecked()));
        onView(withId(R.id.option4)).check(matches(isNotChecked()));

        onView(withId(R.id.option2)).perform(click());
        onView(withId(R.id.option2)).check(matches(isChecked()));
        onView(withId(R.id.option1)).check(matches(isNotChecked()));
    }

    @Test
    public void radio_labeledOptionsShowTextAndAreSelectable() {
        navigateTo(R.id.radioFragment);

        onScrolledView(withId(R.id.option1Label)).check(matches(allOf(withText("Option1"), isNotChecked())));
        onScrolledView(withId(R.id.option4Label)).check(matches(allOf(isNotEnabled(), isChecked(), withText("Option4"))));

        onScrolledView(withId(R.id.option1Label)).perform(click());
        onView(withId(R.id.option1Label)).check(matches(isChecked()));
        onView(withId(R.id.option4Label)).check(matches(isNotChecked()));
    }

    // ---- Checkbox ------------------------------------------------------------------------------

    @Test
    public void checkbox_togglesAndDisabledKeepsState() {
        navigateTo(R.id.checkboxFragment);

        onView(withId(R.id.option2)).check(matches(isNotChecked()));
        onView(withId(R.id.option2)).perform(click());
        onView(withId(R.id.option2)).check(matches(isChecked()));

        onView(withId(R.id.optionLabel)).check(matches(withText("Option label")));
        onView(withId(R.id.optionLabel)).perform(click());
        onView(withId(R.id.optionLabel)).check(matches(isChecked()));

        onView(withId(R.id.optionLabelDisabled)).check(matches(allOf(isNotEnabled(), isChecked())));
    }

    // ---- Text input ----------------------------------------------------------------------------

    @Test
    public void textInput_acceptsTypedText() {
        navigateTo(R.id.textInputFragment);

        onView(allOf(withId(R.id.textInputEditText), isDescendantOfA(withId(R.id.zebra_text_input))))
                .perform(click(), typeText("Hello ZDS"), closeSoftKeyboard())
                .check(matches(withText("Hello ZDS")));
    }

    @Test
    public void textInput_setAndClearErrorUpdatesHelperText() {
        navigateTo(R.id.textInputFragment);

        onView(helperTextOf(R.id.zebra_text_input)).check(matches(withText("Hint")));

        onScrolledView(withId(R.id.buttonSetError)).perform(click());
        onScrolledView(helperTextOf(R.id.zebra_text_input)).check(matches(allOf(isDisplayed(), withText("Error"))));
        onScrolledView(helperTextOf(R.id.zebra_text_input_sharp)).check(matches(allOf(isDisplayed(), withText("Error"))));

        onScrolledView(withId(R.id.buttonClearError)).perform(click());
        onScrolledView(helperTextOf(R.id.zebra_text_input)).check(matches(withText("Hint")));
        onScrolledView(helperTextOf(R.id.zebra_text_input_sharp)).check(matches(withText("Sharp style text area")));
    }

    // ---- Select input --------------------------------------------------------------------------

    @Test
    public void selectInput_setAndClearErrorTogglesErrorText() {
        navigateTo(R.id.selectInputFragment);

        onView(helperTextOf(R.id.zebra_select_input)).check(matches(withEffectiveVisibility(GONE)));

        onScrolledView(withId(R.id.buttonSetError)).perform(click());
        onScrolledView(helperTextOf(R.id.zebra_select_input)).check(matches(allOf(isDisplayed(), withText("Error"))));
        onScrolledView(helperTextOf(R.id.zebra_select_input_sharp)).check(matches(allOf(isDisplayed(), withText("Error"))));

        onScrolledView(withId(R.id.buttonClearError)).perform(click());
        onView(helperTextOf(R.id.zebra_select_input)).check(matches(withEffectiveVisibility(GONE)));
    }

    @Test
    public void selectInput_showsOptionsAndAppliesSelection() {
        navigateTo(R.id.selectInputFragment);

        Matcher<View> editText =
                allOf(withId(R.id.textInputEditText), isDescendantOfA(withId(R.id.zebra_select_input)));
        onView(editText).check(matches(withHint("Placeholder")));

        onView(allOf(withId(com.google.android.material.R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.zebra_select_input)))).perform(click());
        onView(withText("Item 1")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(withText("Item 4")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(withText("Item 2")).inRoot(isPlatformPopup()).perform(click());

        assertEventually(editText, matches(withText("Item 2")));
    }
}
