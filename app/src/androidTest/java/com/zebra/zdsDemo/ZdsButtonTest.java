package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsButtonTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_buttons);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void primaryButtonDispatchesClicks() {
        AtomicBoolean clicked = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsButton button = activity.findViewById(R.id.materialButton);
            button.setOnClickListener(view -> clicked.set(true));
        });

        onView(withId(R.id.materialButton)).perform(click());

        assertTrue(clicked.get());
    }

    @Test
    public void iconAttributeIsAppliedAndTinted() {
        scenario.onActivity(activity -> {
            ZdsButton button = activity.findViewById(R.id.materialButtonIconLeft);
            assertNotNull(button.getIcon());
            assertNotNull(button.getIconTint());
        });
    }

    @Test
    public void disabledButtonUsesTheDisabledPalette() {
        onView(withId(R.id.materialButtonDisabled)).perform(scrollTo()).check(matches(isNotEnabled()));

        scenario.onActivity(activity -> {
            ZdsButton button = activity.findViewById(R.id.materialButtonDisabled);
            int disabledColor = ContextCompat.getColor(activity, com.zebra.zds.R.color.textDisabled);
            int disabledSurface =
                    ContextCompat.getColor(activity, com.zebra.zds.R.color.surfaceDisabled);
            assertEquals(disabledColor, button.getTextColors().getDefaultColor());
            assertEquals(disabledSurface, button.getBackgroundTintList().getDefaultColor());
        });
    }

    @Test
    public void reEnablingRestoresTheEnabledTint() {
        scenario.onActivity(activity -> {
            ZdsButton button = activity.findViewById(R.id.materialButtonDisabled);
            int disabledColor = ContextCompat.getColor(activity, com.zebra.zds.R.color.textDisabled);
            button.setEnabled(true);
            assertTrue(button.isEnabled());
            assertTrue(button.getTextColors().getDefaultColor() != disabledColor);
        });
    }
}
