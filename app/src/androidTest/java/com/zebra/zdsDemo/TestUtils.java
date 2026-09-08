package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.IdRes;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

final class TestUtils {

    private TestUtils() {
    }

    /** Opens the navigation drawer and selects the destination with the given menu id. */
    static void navigateTo(@IdRes int destinationId) {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.navigation_view)).perform(NavigationViewActions.navigateTo(destinationId));
    }

    /** Scrolls the fragment content so the given view is on screen and returns its interaction. */
    static ViewInteraction onScrolledView(Matcher<View> matcher) {
        return onView(matcher).perform(scrollTo());
    }

    /**
     * Re-checks an assertion until it passes or the timeout elapses. Used for views moved by
     * MotionLayout transitions, whose progress Espresso's idle detection does not track.
     */
    static void assertEventually(Matcher<View> matcher, ViewAssertion assertion) {
        long deadline = System.currentTimeMillis() + 3000;
        while (true) {
            try {
                onView(matcher).check(assertion);
                return;
            } catch (AssertionError | NoMatchingViewException e) {
                if (System.currentTimeMillis() > deadline) {
                    throw e;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
    }

    /** Asserts that the collapsing toolbar (which draws its own title) shows the given text. */
    static void assertToolbarTitle(final String title) {
        onView(withId(R.id.collapsingToolbar)).check(matches(withCollapsingTitle(title)));
    }

    static Matcher<View> withCollapsingTitle(final String title) {
        return new BoundedMatcher<View, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override
            protected boolean matchesSafely(CollapsingToolbarLayout layout) {
                return title.contentEquals(String.valueOf(layout.getTitle()));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("collapsing toolbar title is ").appendValue(title);
            }
        };
    }

    static Matcher<View> withProgress(final int progress) {
        return new BoundedMatcher<View, ProgressBar>(ProgressBar.class) {
            @Override
            protected boolean matchesSafely(ProgressBar bar) {
                return bar.getProgress() == progress;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("progress is ").appendValue(progress);
            }
        };
    }

    static ViewAction setSeekBarProgress(final int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription() {
                return "set SeekBar progress to " + progress;
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
                uiController.loopMainThreadUntilIdle();
            }
        };
    }
}
