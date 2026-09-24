package com.zebra.zdsDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.zebra.zds.ZdsBanner;
import com.zebra.zds.ZdsSystemBanner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class BannerDismissTest {

    private Context themedContext() {
        return new ContextThemeWrapper(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                com.zebra.zds.R.style.Theme_ZdsBase);
    }

    private void runOnMain(Runnable runnable) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable);
    }

    @Test
    public void bannerDismissHidesAndNotifies() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ZdsBanner[] holder = new ZdsBanner[1];

        runOnMain(() -> {
            ZdsBanner banner = new ZdsBanner(themedContext());
            banner.setOnDismissListener(dismissed -> latch.countDown());
            holder[0] = banner;
            banner.dismiss();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        runOnMain(() -> assertEquals(View.GONE, holder[0].getVisibility()));
    }

    @Test
    public void systemBannerDismissHidesAndNotifies() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ZdsSystemBanner[] holder = new ZdsSystemBanner[1];

        runOnMain(() -> {
            ZdsSystemBanner banner = new ZdsSystemBanner(themedContext());
            banner.setOnDismissListener(dismissed -> latch.countDown());
            holder[0] = banner;
            banner.dismiss();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        runOnMain(() -> assertEquals(View.GONE, holder[0].getVisibility()));
    }

    @Test
    public void dismissedBannerCanBeShownAgain() {
        runOnMain(() -> {
            ZdsBanner banner = new ZdsBanner(themedContext());
            banner.dismiss();
            assertEquals(View.GONE, banner.getVisibility());
            banner.show();
            assertEquals(View.VISIBLE, banner.getVisibility());
        });
    }

    @Test
    public void showCloseTogglesCloseButton() {
        runOnMain(() -> {
            ZdsBanner banner = new ZdsBanner(themedContext());
            banner.setShowClose(false);
            assertEquals(View.GONE, banner.getCloseButton().getVisibility());
            banner.setShowClose(true);
            assertEquals(View.VISIBLE, banner.getCloseButton().getVisibility());
        });
    }
}
