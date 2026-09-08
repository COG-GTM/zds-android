package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.zebra.zdsDemo.TestUtils.assertEventually;
import static com.zebra.zdsDemo.TestUtils.navigateTo;
import static com.zebra.zdsDemo.TestUtils.onScrolledView;
import static com.zebra.zdsDemo.TestUtils.setSeekBarProgress;
import static com.zebra.zdsDemo.TestUtils.withProgress;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** UI tests for feedback components: dialogs, banners, system banner, snackbar, progress indicators. */
@RunWith(AndroidJUnit4.class)
public class FeedbackComponentsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // ---- Dialogs -------------------------------------------------------------------------------

    @Test
    public void dialog_showsTitleContentAndSingleActionThenDismisses() {
        navigateTo(R.id.dialogsFragment);
        onScrolledView(withId(R.id.oneAction)).perform(click());
        onScrolledView(withId(R.id.showDialogButton)).perform(click());

        onView(withId(R.id.titleText)).inRoot(isDialog()).check(matches(withText("Dialog Title")));
        onView(withId(R.id.contentText)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.positiveButton)).inRoot(isDialog()).check(matches(withText("Confirm")));
        onView(withId(R.id.negativeButton)).inRoot(isDialog()).check(doesNotExist());

        onView(withId(R.id.positiveButton)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.titleText)).check(doesNotExist());
    }

    @Test
    public void dialog_threeActionsWithIconRendersAllButtons() {
        navigateTo(R.id.dialogsFragment);
        onScrolledView(withId(R.id.titleImage)).perform(click());
        onScrolledView(withId(R.id.threeActions)).perform(click());
        onScrolledView(withId(R.id.showDialogButton)).perform(click());

        onView(withId(R.id.titleIcon)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.positiveButton)).inRoot(isDialog()).check(matches(withText("Confirm")));
        onView(withId(R.id.negativeButton)).inRoot(isDialog()).check(matches(withText("Cancel")));
        onView(withId(R.id.neutralButton)).inRoot(isDialog()).check(matches(withText("Learn More")));

        onView(withId(R.id.negativeButton)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.titleText)).check(doesNotExist());
    }

    // ---- Banner --------------------------------------------------------------------------------

    @Test
    public void banner_showsOnButtonClickWithTitleAndActions() {
        navigateTo(R.id.bannerFragment);

        onView(withId(R.id.banner)).check(matches(not(isDisplayed())));
        onScrolledView(withId(R.id.showBanner)).perform(click());

        assertEventually(withId(R.id.banner), matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.banner))))
                .check(matches(allOf(isDisplayed(), withText("Banner title"))));
        onView(allOf(withId(R.id.actionOne), isDescendantOfA(withId(R.id.banner))))
                .check(matches(withText("Action 1")));
        onView(allOf(withId(R.id.actionTwo), isDescendantOfA(withId(R.id.banner))))
                .check(matches(withText("Action 2")));
        onView(allOf(withId(R.id.close), isDescendantOfA(withId(R.id.banner))))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.close), isDescendantOfA(withId(R.id.banner)))).perform(click());
        assertEventually(withId(R.id.banner), matches(not(isDisplayed())));
    }

    @Test
    public void banner_styleRadioButtonsAreSelectable() {
        navigateTo(R.id.bannerFragment);

        onScrolledView(withId(R.id.warning)).perform(click());
        onView(withId(R.id.warning)).check(matches(isChecked()));
        onView(withId(R.id.none)).check(matches(isNotChecked()));

        onScrolledView(withId(R.id.showBanner)).perform(click());
        assertEventually(withId(R.id.banner), matches(isDisplayed()));
    }

    // ---- System banner -------------------------------------------------------------------------

    @Test
    public void systemBanner_showsFromBannerScreenAndHidesOnTap() {
        navigateTo(R.id.bannerFragment);

        onView(withId(R.id.systemBanner)).check(matches(not(isDisplayed())));
        onScrolledView(withId(R.id.showSystemBanner)).perform(click());

        assertEventually(withId(R.id.systemBanner), matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.systemBanner))))
                .check(matches(allOf(isDisplayed(), withText("Banner title"))));

        onView(withId(R.id.systemBanner)).perform(click());
        assertEventually(withId(R.id.systemBanner), matches(not(isDisplayed())));
    }

    // ---- Snackbar ------------------------------------------------------------------------------

    @Test
    public void snackbar_showsMessageAndAction() {
        navigateTo(R.id.snackbarFragment);

        onView(withId(R.id.show_snackbar)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("This is a snackbar")));
        onView(withId(com.google.android.material.R.id.snackbar_action))
                .check(matches(withText("Action")));
    }

    // ---- Progress indicators -------------------------------------------------------------------

    @Test
    public void progressIndicators_reflectSeekBarValue() {
        navigateTo(R.id.progressIndicatorsFragment);

        onView(withId(R.id.progressSeekBar)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.textLabel), isDescendantOfA(withId(R.id.progressIndicatorLinear))))
                .check(matches(withText("50% Complete")));
        onView(allOf(withId(R.id.progressIndicator), isDescendantOfA(withId(R.id.progressIndicatorLinear))))
                .check(matches(withProgress(50)));
        onScrolledView(allOf(withId(R.id.textLabel), isDescendantOfA(withId(R.id.progressIndicatorCircular))))
                .check(matches(withText("50%")));

        onView(withId(R.id.progressSeekBar)).perform(setSeekBarProgress(100));

        onView(allOf(withId(R.id.textLabel), isDescendantOfA(withId(R.id.progressIndicatorLinear))))
                .check(matches(withText("100% Complete")));
        onView(allOf(withId(R.id.progressIndicator), isDescendantOfA(withId(R.id.progressIndicatorLinear))))
                .check(matches(withProgress(100)));
        onScrolledView(allOf(withId(R.id.actionView), isDescendantOfA(withId(R.id.progressIndicatorCircular))))
                .check(matches(isDisplayed()));
    }
}
