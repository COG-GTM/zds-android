package com.zebra.zdsDemo;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/** Shared helpers for the demo app instrumentation tests. */
final class ZdsTestSupport {

    private ZdsTestSupport() {
    }

    /** Navigates the demo app to a destination of the nav graph and waits for it to be laid out. */
    static void navigateTo(ActivityScenario<MainActivity> scenario, @IdRes int destinationId) {
        scenario.onActivity(activity -> {
            NavController navController =
                    Navigation.findNavController(activity.findViewById(R.id.nav_host_fragment));
            navController.navigate(destinationId);
        });
    }

    /** Matches a view whose {@code isChecked()} state equals {@code checked}, for any checkable view. */
    static Matcher<View> withCheckedState(boolean checked) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is a checkable view that is " + (checked ? "checked" : "not checked"));
            }

            @Override
            protected boolean matchesSafely(View view) {
                return view instanceof android.widget.Checkable
                        && ((android.widget.Checkable) view).isChecked() == checked;
            }
        };
    }

    /** Matches a {@link com.google.android.material.tabs.TabLayout} whose selected tab is at {@code index}. */
    static Matcher<View> withSelectedTab(int index) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is a TabLayout with tab " + index + " selected");
            }

            @Override
            protected boolean matchesSafely(View view) {
                return view instanceof com.google.android.material.tabs.TabLayout
                        && ((com.google.android.material.tabs.TabLayout) view).getSelectedTabPosition() == index;
            }
        };
    }
}
