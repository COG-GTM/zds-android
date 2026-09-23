package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsDialog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsDialogTest {

    private ActivityScenario<ZdsHostActivity> scenario;
    private final AtomicReference<ZdsDialog> dialog = new AtomicReference<>();

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_buttons);
    }

    @After
    public void tearDown() {
        scenario.onActivity(activity -> {
            ZdsDialog shown = dialog.get();
            if (shown != null && shown.isShowing()) {
                shown.dismiss();
            }
        });
        scenario.close();
    }

    @Test
    public void positiveActionRunsTheListenerAndDismisses() {
        AtomicBoolean confirmed = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsDialog built = new ZdsDialog.Builder(activity)
                    .setTitle("Delete profile")
                    .setContent("This cannot be undone.")
                    .setIcon(com.zebra.zds.R.drawable.ic_warning_round)
                    .setPositiveAction("Confirm", view -> confirmed.set(true))
                    .build();
            dialog.set(built);
            built.show();
        });

        onView(withId(com.zebra.zds.R.id.titleText)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(com.zebra.zds.R.id.contentText)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(com.zebra.zds.R.id.titleIcon)).inRoot(isDialog()).check(matches(isDisplayed()));

        onView(withId(com.zebra.zds.R.id.positiveButton)).inRoot(isDialog()).perform(click());

        assertTrue(confirmed.get());
        assertFalse(dialog.get().isShowing());
    }

    @Test
    public void negativeActionDismissesWithoutRunningThePositiveListener() {
        AtomicBoolean confirmed = new AtomicBoolean(false);
        AtomicBoolean cancelled = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsDialog built = new ZdsDialog.Builder(activity)
                    .setTitle("Delete profile")
                    .setContent("This cannot be undone.")
                    .setPositiveAction("Confirm", view -> confirmed.set(true))
                    .setNegativeAction("Cancel", view -> cancelled.set(true))
                    .setIsSharp(true)
                    .build();
            dialog.set(built);
            built.show();
        });

        onView(withId(com.zebra.zds.R.id.negativeButton)).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        assertTrue(cancelled.get());
        assertFalse(confirmed.get());
        assertFalse(dialog.get().isShowing());
    }

    @Test
    public void threeActionDialogShowsEveryButton() {
        scenario.onActivity(activity -> {
            ZdsDialog built = new ZdsDialog.Builder(activity)
                    .setTitle("Sync")
                    .setContent("Sync pending changes?")
                    .setPositiveAction("Yes", null)
                    .setNegativeAction("No", null)
                    .setNeutralAction("Later", null)
                    .build();
            dialog.set(built);
            built.show();
        });

        onView(withId(com.zebra.zds.R.id.positiveButton)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(com.zebra.zds.R.id.negativeButton)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(com.zebra.zds.R.id.neutralButton)).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        assertFalse(dialog.get().isShowing());
    }

    @Test
    public void dismissHidesTheDialog() {
        scenario.onActivity(activity -> {
            ZdsDialog built = new ZdsDialog.Builder(activity)
                    .setTitle("Info")
                    .setContent("Nothing to do.")
                    .build();
            dialog.set(built);
            built.show();
            assertTrue(built.isShowing());
        });

        onView(withId(com.zebra.zds.R.id.titleText)).inRoot(isDialog()).check(matches(isDisplayed()));

        scenario.onActivity(activity -> dialog.get().dismiss());
        scenario.onActivity(activity -> assertFalse(dialog.get().isShowing()));
    }
}
