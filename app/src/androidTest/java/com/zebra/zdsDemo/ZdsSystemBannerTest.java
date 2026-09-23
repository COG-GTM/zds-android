package com.zebra.zdsDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsSystemBanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsSystemBannerTest {

    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void systemBannerStartsWithTheDefaultStyle() {
        scenario.onActivity(activity -> {
            ZdsSystemBanner banner = activity.getBanner();
            assertNotNull(banner);
            assertEquals(ZdsSystemBanner.Style.DEFAULT, banner.getStyle());
        });
    }

    @Test
    public void showBannerAppliesTheStyleAndTitle() {
        scenario.onActivity(activity -> {
            ZdsSystemBanner banner = activity.getBanner();
            banner.setStyle(ZdsSystemBanner.Style.WARNING);
            banner.setTitleString("Battery low");
            activity.showBanner();

            assertEquals(View.VISIBLE, banner.getVisibility());
            assertEquals("Battery low", banner.getTitle().getText().toString());
            assertEquals(ZdsSystemBanner.Style.WARNING, banner.getStyle());
        });
    }

    @Test
    public void centerTitleKeepsTheTitleAttached() {
        scenario.onActivity(activity -> {
            ZdsSystemBanner banner = activity.getBanner();
            banner.setTitleString("Sync in progress");
            banner.centerTitle(true);
            assertEquals("Sync in progress", banner.getTitle().getText().toString());

            banner.centerTitle(false);
            assertEquals("Sync in progress", banner.getTitle().getText().toString());
        });
    }
}
