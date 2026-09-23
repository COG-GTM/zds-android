package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsCircularProgressIndicator;
import com.zebra.zds.ZdsLinearProgressIndicator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsProgressIndicatorTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_progress_indicators);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void linearIndicatorParsesProgressMaxAndBuffering() {
        scenario.onActivity(activity -> {
            ZdsLinearProgressIndicator indicator =
                    activity.findViewById(R.id.progressIndicatorLinear);
            assertEquals(100, indicator.getProgressIndicator().getMax());
            assertEquals(50, indicator.getProgressIndicator().getProgress());
            assertFalse(indicator.getProgressIndicator().isIndeterminate());
            assertEquals(View.VISIBLE, indicator.getProgressLayout().getVisibility());
        });
    }

    @Test
    public void linearIndicatorHidesTheBufferingLayoutWhenNotRequested() {
        scenario.onActivity(activity -> {
            ZdsLinearProgressIndicator indicator =
                    activity.findViewById(R.id.progressIndicatorLinearNoLabel);
            assertEquals(View.GONE, indicator.getProgressLayout().getVisibility());
            assertEquals(View.GONE, indicator.getLabelText().getVisibility());
        });
    }

    @Test
    public void linearIndicatorLabelAndProgressCanBeUpdated() {
        scenario.onActivity(activity -> {
            ZdsLinearProgressIndicator indicator =
                    activity.findViewById(R.id.progressIndicatorLinearIndeterminate);
            assertEquals("Indeterminate", indicator.getLabelString());
            assertTrue(indicator.getProgressIndicator().isIndeterminate());

            indicator.setLabel("Downloading");
            assertEquals("Downloading", indicator.getLabelText().getText().toString());
            assertEquals(View.VISIBLE, indicator.getLabelText().getVisibility());
        });

        scenario.onActivity(activity -> {
            ZdsLinearProgressIndicator indicator =
                    activity.findViewById(R.id.progressIndicatorLinear);
            indicator.setProgress(75);
            assertEquals(75, indicator.getProgressIndicator().getProgress());
        });
    }

    @Test
    public void circularIndicatorParsesProgressAndIndeterminate() {
        scenario.onActivity(activity -> {
            ZdsCircularProgressIndicator determinate =
                    activity.findViewById(R.id.progressIndicatorCircular);
            assertEquals(100, determinate.getProgressIndicator().getMax());
            assertEquals(50, determinate.getProgressIndicator().getProgress());
            assertFalse(determinate.getProgressIndicator().isIndeterminate());

            ZdsCircularProgressIndicator indeterminate =
                    activity.findViewById(R.id.progressIndicatorCircularIndeterminate);
            assertTrue(indeterminate.getProgressIndicator().isIndeterminate());
        });
    }

    @Test
    public void circularIndicatorActionViewDispatchesClicks() {
        AtomicBoolean clicked = new AtomicBoolean(false);

        scenario.onActivity(activity -> {
            ZdsCircularProgressIndicator indicator =
                    activity.findViewById(R.id.progressIndicatorCircular);
            indicator.enableActionView(true);
            indicator.setActionListener(view -> clicked.set(true));
            assertEquals(View.VISIBLE, indicator.getActionView().getVisibility());
        });

        onView(allOf(withId(com.zebra.zds.R.id.actionView),
                isDescendantOfA(withId(R.id.progressIndicatorCircular))))
                .perform(scrollTo(), click());

        assertTrue(clicked.get());
    }
}
