package com.zebra.zdsDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.FrameLayout;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsListItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsListItemTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(ZdsHostActivity.class);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void listItemInflatesWithItsSubViews() {
        scenario.onActivity(activity -> {
            ZdsListItem.ZdsViewHolder holder = newHolder(activity);
            assertNotNull(holder.getContent());
            assertNotNull(holder.getDescriptor());
            assertNotNull(holder.getMeta());
            assertNotNull(holder.getIconLeft());
            assertNotNull(holder.getIconRight());
            assertNotNull(holder.getActionView());
        });
    }

    @Test
    public void iconVisibilityFollowsTheHolderApi() {
        scenario.onActivity(activity -> {
            ZdsListItem.ZdsViewHolder holder = newHolder(activity);

            holder.showIconLeft(com.zebra.zds.R.drawable.ic_point_star_round);
            assertEquals(View.VISIBLE, holder.getIconLeft().getVisibility());
            holder.hideIconLeft();
            assertEquals(View.GONE, holder.getIconLeft().getVisibility());

            holder.showIconRight(com.zebra.zds.R.drawable.ic_point_star_round);
            assertEquals(View.VISIBLE, holder.getIconRight().getVisibility());
            assertEquals(View.GONE, holder.getMeta().getVisibility());
            assertEquals(View.GONE, holder.getActionView().getVisibility());
        });
    }

    @Test
    public void metaTextHidesTheTrailingIconAndActionView() {
        scenario.onActivity(activity -> {
            ZdsListItem.ZdsViewHolder holder = newHolder(activity);
            holder.showIconRight(com.zebra.zds.R.drawable.ic_point_star_round);

            holder.setMeta("12 items");
            assertEquals("12 items", holder.getMeta().getText().toString());
            assertEquals(View.VISIBLE, holder.getMeta().getVisibility());
            assertEquals(View.GONE, holder.getIconRight().getVisibility());
            assertEquals(View.GONE, holder.getActionView().getVisibility());
        });
    }

    @Test
    public void actionViewIsACheckableZdsCheckBox() {
        scenario.onActivity(activity -> {
            ZdsListItem.ZdsViewHolder holder = newHolder(activity);

            holder.showActionView();
            assertEquals(View.VISIBLE, holder.getActionView().getVisibility());

            holder.getActionView().setChecked(true);
            assertTrue(holder.getActionView().isChecked());

            holder.hideActionView();
            assertEquals(View.GONE, holder.getActionView().getVisibility());
        });
    }

    private static ZdsListItem.ZdsViewHolder newHolder(ZdsHostActivity activity) {
        View itemView = ZdsListItem.Companion.getListItem(new FrameLayout(activity));
        return new ZdsListItem.ZdsViewHolder(itemView);
    }
}
