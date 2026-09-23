package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.view.MenuItem;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsSearchView;
import com.zebra.zds.ZdsToolbar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsToolbarTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_toolbar);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void toolbarAttributesAreParsedFromXml() {
        scenario.onActivity(activity -> {
            ZdsToolbar toolbar = activity.findViewById(R.id.toolbar_avatar);
            assertEquals(activity.getString(R.string.app_name), toolbar.getTitle().toString());
            assertTrue(toolbar.isTitleCentered());
            assertNotNull(toolbar.getNavigationIcon());
            assertNotNull(toolbar.getLogo());
            assertNotNull(toolbar.getMenu().findItem(R.id.action_change_theme));
        });

        onView(allOf(withText(R.string.app_name), isDescendantOfA(withId(R.id.toolbar_avatar))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void menuItemClicksAreDispatched() {
        AtomicBoolean clicked = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsToolbar toolbar = activity.findViewById(R.id.toolbar_avatar);
            toolbar.setOnMenuItemClickListener(item -> {
                clicked.set(item.getItemId() == R.id.action_change_theme);
                return true;
            });
        });

        onView(allOf(withId(R.id.action_change_theme), isDescendantOfA(withId(R.id.toolbar_avatar))))
                .perform(click());

        assertTrue(clicked.get());
    }

    @Test
    public void navigationIconClicksAreDispatched() {
        AtomicBoolean navigated = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsToolbar toolbar = activity.findViewById(R.id.toolbar_avatar);
            toolbar.setNavigationContentDescription("Open drawer");
            toolbar.setNavigationOnClickListener(view -> navigated.set(true));
        });

        onView(allOf(withContentDescription("Open drawer"),
                isDescendantOfA(withId(R.id.toolbar_avatar))))
                .perform(click());

        assertTrue(navigated.get());
    }

    @Test
    public void searchMenuUsesTheZdsSearchViewActionView() {
        scenario.onActivity(activity -> {
            ZdsToolbar toolbar = activity.findViewById(R.id.toolbar_search);
            MenuItem item = toolbar.getMenu().findItem(R.id.action_search);
            assertNotNull(item);
            assertTrue(item.getActionView() instanceof ZdsSearchView);
        });
    }
}
