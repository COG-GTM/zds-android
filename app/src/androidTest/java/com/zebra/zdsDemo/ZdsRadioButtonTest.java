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
import static org.junit.Assert.assertTrue;

import android.widget.RadioGroup;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsRadioButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsRadioButtonTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_radio);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void selectingOneOptionDeselectsTheOthers() {
        onView(withId(R.id.option1)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.option2)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.option1)).check(matches(isNotChecked()));

        scenario.onActivity(activity -> {
            RadioGroup group = activity.findViewById(R.id.radioGroup);
            assertEquals(R.id.option2, group.getCheckedRadioButtonId());
        });
    }

    @Test
    public void disabledOptionCannotBeSelected() {
        onView(withId(R.id.option3))
                .check(matches(isNotEnabled()))
                .perform(click())
                .check(matches(isNotChecked()));
    }

    @Test
    public void disabledButCheckedOptionKeepsTheEnabledButtonDrawable() {
        scenario.onActivity(activity -> {
            ZdsRadioButton checkedDisabled = activity.findViewById(R.id.option4);
            assertTrue(checkedDisabled.isChecked());
            assertNotNull(checkedDisabled.getButtonDrawable());
        });
    }

    @Test
    public void labelledOptionsCarryTheirXmlText() {
        scenario.onActivity(activity -> {
            ZdsRadioButton labelled = activity.findViewById(R.id.option1Label);
            assertEquals("Option1", labelled.getText().toString());
        });

        onView(withId(R.id.option2Label)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.option1Label)).check(matches(isNotChecked()));
    }
}
