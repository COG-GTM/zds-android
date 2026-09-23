package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.zebra.zds.ZdsCheckBox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsCheckBoxTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_checkbox);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void indeterminateStateIsParsedFromXml() {
        scenario.onActivity(activity -> {
            ZdsCheckBox checkBox = activity.findViewById(R.id.option1);
            assertEquals(MaterialCheckBox.STATE_INDETERMINATE, checkBox.getCheckedState());
        });
    }

    @Test
    public void tappingTogglesCheckedState() {
        onView(withId(R.id.option2)).check(matches(isNotChecked())).perform(click());
        onView(withId(R.id.option2)).check(matches(isChecked()));

        scenario.onActivity(activity -> {
            ZdsCheckBox checkBox = activity.findViewById(R.id.option2);
            assertTrue(checkBox.isChecked());
        });

        onView(withId(R.id.option2)).perform(click()).check(matches(isNotChecked()));
    }

    @Test
    public void labelIsTakenFromXml() {
        scenario.onActivity(activity -> {
            ZdsCheckBox checkBox = activity.findViewById(R.id.optionLabel);
            assertEquals("Option label", checkBox.getText().toString());
        });
    }

    @Test
    public void disabledCheckBoxKeepsItsStateWhenTapped() {
        onView(withId(R.id.optionLabelDisabled))
                .check(matches(isNotEnabled()))
                .check(matches(isChecked()))
                .perform(click())
                .check(matches(isChecked()));

        scenario.onActivity(activity -> {
            ZdsCheckBox checkBox = activity.findViewById(R.id.optionLabelDisabled);
            assertFalse(checkBox.isEnabled());
        });
    }
}
