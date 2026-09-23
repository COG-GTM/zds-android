package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsSwitch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsSwitchTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_switch);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void tappingTogglesTheSwitch() {
        onView(withId(R.id.zebra_switch)).check(matches(isNotChecked())).perform(click());
        onView(withId(R.id.zebra_switch)).check(matches(isChecked())).perform(click());
        onView(withId(R.id.zebra_switch)).check(matches(isNotChecked()));
    }

    @Test
    public void disabledSwitchIgnoresTapsAndUsesTheDisabledTint() {
        onView(withId(R.id.zebra_switch_disabled))
                .check(matches(isNotEnabled()))
                .perform(click())
                .check(matches(isNotChecked()));

        scenario.onActivity(activity -> {
            ZdsSwitch disabled = activity.findViewById(R.id.zebra_switch_disabled);
            assertNotNull(disabled.getThumbTintList());
            assertEquals(
                    ContextCompat.getColor(activity, com.zebra.zds.R.color.textDisabled),
                    disabled.getThumbTintList().getDefaultColor());
            assertEquals(
                    ContextCompat.getColor(activity, com.zebra.zds.R.color.surfaceDisabled),
                    disabled.getTrackTintList().getDefaultColor());
        });
    }
}
