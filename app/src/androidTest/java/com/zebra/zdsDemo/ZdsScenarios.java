package com.zebra.zdsDemo;

import androidx.annotation.LayoutRes;
import androidx.test.core.app.ActivityScenario;

final class ZdsScenarios {

    private ZdsScenarios() {
    }

    static ActivityScenario<ZdsHostActivity> launchWith(@LayoutRes int layoutResId) {
        ActivityScenario<ZdsHostActivity> scenario = ActivityScenario.launch(ZdsHostActivity.class);
        scenario.onActivity(activity -> activity.setContent(layoutResId));
        return scenario;
    }
}
