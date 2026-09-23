package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsSearchView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsSearchViewTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_search);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void inflatesWithTheZdsBackground() {
        scenario.onActivity(activity -> {
            ZdsSearchView search = activity.findViewById(R.id.zebra_search);
            assertNotNull(search.getBackground());
        });
    }

    @Test
    public void typingUpdatesTheQueryAndNotifiesTheListener() {
        AtomicReference<String> lastChange = new AtomicReference<>();

        scenario.onActivity(activity -> {
            ZdsSearchView search = activity.findViewById(R.id.zebra_search);
            search.setIconifiedByDefault(false);
            search.setIconified(false);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    lastChange.set(newText);
                    return false;
                }
            });
        });

        onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(click(), typeText("label"), closeSoftKeyboard());

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("label")));
        assertEquals("label", lastChange.get());

        scenario.onActivity(activity -> {
            ZdsSearchView search = activity.findViewById(R.id.zebra_search);
            assertEquals("label", search.getQuery().toString());
        });
    }

    @Test
    public void focusSwapsTheSelectedBackground() {
        scenario.onActivity(activity -> {
            ZdsSearchView search = activity.findViewById(R.id.zebra_search);
            search.setIconifiedByDefault(false);
            search.setIconified(false);
        });

        onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(click())
                .check(matches(isDisplayed()));

        scenario.onActivity(activity -> {
            ZdsSearchView search = activity.findViewById(R.id.zebra_search);
            assertTrue(search.findFocus() != null);
        });
    }
}
