package com.zebra.zdsDemo;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zebra.zds.R;

/**
 * Hosts a single demo layout under the ZDS base theme so instrumentation tests can exercise one
 * component at a time.
 */
public class ZdsHostActivity extends AppCompatActivity {

    private FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_ZdsBase);
        super.onCreate(savedInstanceState);

        container = new FrameLayout(this);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(container);
        setContentView(scrollView);
    }

    public View setContent(@LayoutRes int layoutResId) {
        container.removeAllViews();
        return getLayoutInflater().inflate(layoutResId, container, true);
    }
}
