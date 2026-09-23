package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsBanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsBannerTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_banner);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void bannerStartsWithTheDefaultStyleAndNoActions() {
        scenario.onActivity(activity -> {
            ZdsBanner banner = activity.findViewById(R.id.banner);
            assertEquals(ZdsBanner.Style.DEFAULT, banner.getStyle());
            assertEquals(View.GONE, banner.getActionButtonOne().getVisibility());
            assertEquals(View.GONE, banner.getActionButtonTwo().getVisibility());
        });
    }

    @Test
    public void titleAndMessageAreRenderedWhenTheBannerIsShown() {
        scenario.onActivity(activity -> {
            ZdsBanner banner = activity.findViewById(R.id.banner);
            banner.setTitleString("Printer offline");
            banner.setMessageString("Reconnect the printer to continue.");
            banner.setStyle(ZdsBanner.Style.NEGATIVE);
            show(activity, banner);
        });

        scenario.onActivity(activity -> {
            ZdsBanner banner = activity.findViewById(R.id.banner);
            assertEquals("Printer offline", banner.getTitle().getText().toString());
            assertEquals(
                    "Reconnect the printer to continue.",
                    banner.getMessage().getText().toString());
            assertEquals(ZdsBanner.Style.NEGATIVE, banner.getStyle());
            assertNotNull(banner.getTitleImage().getImageTintList());
        });

        onView(withId(R.id.banner)).check(matches(isDisplayed()));
    }

    @Test
    public void actionsBecomeVisibleAndDispatchClicks() {
        AtomicBoolean actionOne = new AtomicBoolean(false);
        AtomicBoolean closed = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsBanner banner = activity.findViewById(R.id.banner);
            banner.setTitleString("Update available");
            show(activity, banner);
            banner.setActionOne("Retry", view -> actionOne.set(true));
            banner.setActionTwo("Dismiss", null);
            banner.setCloseAction(view -> closed.set(true));
        });

        scenario.onActivity(activity -> {
            ZdsBanner banner = activity.findViewById(R.id.banner);
            assertEquals(View.VISIBLE, banner.getActionButtonOne().getVisibility());
            assertEquals("Retry", banner.getActionButtonOne().getText().toString());
            assertEquals(View.GONE, banner.getActionButtonTwo().getVisibility());
        });

        onView(allOf(withId(com.zebra.zds.R.id.actionOne), isDescendantOfA(withId(R.id.banner))))
                .perform(click());
        onView(allOf(withId(com.zebra.zds.R.id.close), isDescendantOfA(withId(R.id.banner))))
                .perform(click());

        assertTrue(actionOne.get());
        assertTrue(closed.get());
    }

    /** Mirrors BannerFragment: the banner is off-screen until the motion scene reaches its end. */
    private static void show(ZdsHostActivity activity, ZdsBanner banner) {
        banner.setVisibility(View.VISIBLE);
        ((MotionLayout) activity.findViewById(R.id.motionLayout)).setProgress(1f);
    }

    @Test
    public void sharpStyleRemovesTheCardCornerRadius() {
        scenario.onActivity(activity -> {
            ZdsBanner banner = activity.findViewById(R.id.banner);
            assertTrue(banner.getCard().getRadius() > 0f);
            banner.setSharp(true);
            assertEquals(0f, banner.getCard().getRadius(), 0f);
        });
    }
}
