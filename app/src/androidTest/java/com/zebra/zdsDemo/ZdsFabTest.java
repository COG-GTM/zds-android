package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsExtendedFAB;
import com.zebra.zds.ZdsFAB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsFabTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_fab);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void fabDispatchesClicksAndCarriesItsIcon() {
        AtomicBoolean clicked = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsFAB fab = activity.findViewById(R.id.materialButton);
            assertNotNull(fab.getDrawable());
            fab.setOnClickListener(view -> clicked.set(true));
        });

        onView(withId(R.id.materialButton)).perform(scrollTo(), click());

        assertTrue(clicked.get());
    }

    @Test
    public void disabledFabUsesTheDisabledImageTint() {
        onView(withId(R.id.materialButtonDisabled)).perform(scrollTo()).check(matches(isNotEnabled()));

        scenario.onActivity(activity -> {
            ZdsFAB fab = activity.findViewById(R.id.materialButtonDisabled);
            assertEquals(
                    ContextCompat.getColor(activity, com.zebra.zds.R.color.textDisabled),
                    fab.getImageTintList().getDefaultColor());
        });
    }

    @Test
    public void extendedFabKeepsItsLabelAndShrinks() {
        scenario.onActivity(activity -> {
            ZdsExtendedFAB fab = activity.findViewById(R.id.materialButtonExtended);
            assertEquals("Label", fab.getText().toString());
            assertNotNull(fab.getIcon());
            fab.shrink();
            assertFalse(fab.isExtended());
            fab.extend();
        });
    }

    @Test
    public void disabledExtendedFabUsesTheDisabledTextTint() {
        onView(withId(R.id.materialButtonExtendedDisabled)).perform(scrollTo())
                .check(matches(isNotEnabled()));

        scenario.onActivity(activity -> {
            ZdsExtendedFAB fab = activity.findViewById(R.id.materialButtonExtendedDisabled);
            assertEquals(
                    ContextCompat.getColor(activity, com.zebra.zds.R.color.textDisabled),
                    fab.getTextColors().getDefaultColor());
        });
    }
}
