package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.tabs.TabLayout;
import com.zebra.zds.ZdsTabLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsTabLayoutTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_tabs);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void tabItemsFromXmlBecomeTabs() {
        scenario.onActivity(activity -> {
            ZdsTabLayout tabs = activity.findViewById(R.id.tabLayout);
            assertEquals(3, tabs.getTabCount());
            assertEquals("Tab1", tabs.getTabAt(0).getText().toString());
            assertEquals("Tab3", tabs.getTabAt(2).getText().toString());
            assertNotNull(tabs.getTabAt(0).getIcon());
            assertEquals(TabLayout.MODE_FIXED, tabs.getTabMode());

            ZdsTabLayout scrollable = activity.findViewById(R.id.tabLayoutContinuous);
            assertEquals(6, scrollable.getTabCount());
            assertEquals(TabLayout.MODE_SCROLLABLE, scrollable.getTabMode());
        });
    }

    @Test
    public void tappingATabSelectsItAndNotifiesTheListener() {
        AtomicInteger selectedPosition = new AtomicInteger(-1);

        scenario.onActivity(activity -> {
            ZdsTabLayout tabs = activity.findViewById(R.id.tabLayout);
            tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    selectedPosition.set(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            assertEquals(0, tabs.getSelectedTabPosition());
        });

        onView(allOf(withText("Tab3"), isDescendantOfA(withId(R.id.tabLayout)))).perform(click());

        scenario.onActivity(activity -> {
            ZdsTabLayout tabs = activity.findViewById(R.id.tabLayout);
            assertEquals(2, tabs.getSelectedTabPosition());
            assertTrue(tabs.getTabAt(2).isSelected());
        });
        assertEquals(2, selectedPosition.get());
    }
}
