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

import com.zebra.zds.ZdsIconButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsIconButtonTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_icon_buttons);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void iconButtonInflatesWithAnIconAndDispatchesClicks() {
        AtomicBoolean clicked = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsIconButton button = activity.findViewById(R.id.materialButton);
            assertNotNull(button.getIcon());
            button.setOnClickListener(view -> clicked.set(true));
        });

        onView(withId(R.id.materialButton)).perform(click());

        assertTrue(clicked.get());
    }

    @Test
    public void disabledIconButtonUsesTheDisabledIconTint() {
        onView(withId(R.id.materialButtonDisabled)).perform(scrollTo()).check(matches(isNotEnabled()));

        scenario.onActivity(activity -> {
            ZdsIconButton button = activity.findViewById(R.id.materialButtonDisabled);
            assertEquals(
                    ContextCompat.getColor(activity, com.zebra.zds.R.color.textDisabled),
                    button.getIconTint().getDefaultColor());
        });
    }
}
