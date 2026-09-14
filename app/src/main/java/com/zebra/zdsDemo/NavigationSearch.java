package com.zebra.zdsDemo;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import com.zebra.zds.ZdsSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Filters the navigation drawer menu as the user types, hiding items whose titles do not match the
 * query and showing an empty state when nothing matches.
 */
public class NavigationSearch {

    private final NavigationView navigationView;
    private final ZdsSearchView searchView;
    private final TextView emptyState;

    private NavigationSearch(@NonNull NavigationView navigationView, @NonNull View header) {
        this.navigationView = navigationView;
        this.searchView = header.findViewById(R.id.navigation_search);
        this.emptyState = header.findViewById(R.id.navigation_search_empty);
    }

    public static NavigationSearch attach(@NonNull NavigationView navigationView) {
        View header = navigationView.inflateHeaderView(R.layout.navigation_search);
        NavigationSearch search = new NavigationSearch(navigationView, header);

        ZdsSearchView searchView = search.searchView;
        searchView.setOnQueryTextListener(new ZdsSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return search.navigateToSingleMatch();
            }

            @Override
            public boolean onQueryTextChange(String query) {
                search.filter(query);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            search.filter(null);
            return false;
        });
        return search;
    }

    /**
     * Clears the query and shows every menu item again, e.g. after the drawer closes on a selection.
     */
    public void reset() {
        searchView.setQuery("", false);
        searchView.clearFocus();
        filter(null);
    }

    private void filter(String query) {
        String normalized = query == null ? "" : query.trim().toLowerCase(Locale.getDefault());
        int visibleLeaves = applyFilter(navigationView.getMenu(), normalized);
        emptyState.setVisibility(visibleLeaves == 0 ? View.VISIBLE : View.GONE);
    }

    private int applyFilter(Menu menu, String query) {
        int visibleLeaves = 0;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (isSpacer(item)) {
                continue;
            }
            SubMenu subMenu = item.getSubMenu();
            if (subMenu != null) {
                int visibleChildren = applyFilter(subMenu, query);
                item.setVisible(visibleChildren > 0);
                visibleLeaves += visibleChildren;
            } else {
                boolean matches = matches(item, query);
                item.setVisible(matches);
                syncActionView(item, matches);
                if (matches) {
                    visibleLeaves++;
                }
            }
        }
        return visibleLeaves;
    }

    /**
     * Keeps a badge from lingering on a recycled row once its own item is filtered out.
     */
    private void syncActionView(MenuItem item, boolean itemVisible) {
        View actionView = item.getActionView();
        if (!(actionView instanceof TextView)) {
            return;
        }
        CharSequence badge = ((TextView) actionView).getText();
        boolean showBadge = itemVisible && badge != null && badge.length() > 0;
        actionView.setVisibility(showBadge ? View.VISIBLE : View.GONE);
    }

    private boolean navigateToSingleMatch() {
        MenuItem match = singleVisibleLeaf(navigationView.getMenu());
        if (match == null) {
            return false;
        }
        navigationView.getMenu().performIdentifierAction(match.getItemId(), 0);
        return true;
    }

    private MenuItem singleVisibleLeaf(Menu menu) {
        List<MenuItem> leaves = new ArrayList<>();
        collectVisibleLeaves(menu, leaves, 2);
        return leaves.size() == 1 ? leaves.get(0) : null;
    }

    private void collectVisibleLeaves(Menu menu, List<MenuItem> leaves, int limit) {
        for (int i = 0; i < menu.size() && leaves.size() < limit; i++) {
            MenuItem item = menu.getItem(i);
            if (isSpacer(item) || !item.isVisible()) {
                continue;
            }
            if (item.getSubMenu() != null) {
                collectVisibleLeaves(item.getSubMenu(), leaves, limit);
            } else {
                leaves.add(item);
            }
        }
    }

    private boolean matches(MenuItem item, String query) {
        if (query.isEmpty()) {
            return true;
        }
        CharSequence title = item.getTitle();
        return title != null && title.toString().toLowerCase(Locale.getDefault()).contains(query);
    }

    private boolean isSpacer(MenuItem item) {
        return item.getItemId() == R.id.footer_spacer_1 || item.getItemId() == R.id.footer_spacer_2;
    }
}
