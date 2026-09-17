package com.zebra.zdsDemo.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.zebra.zds.ZdsSearchView;
import com.zebra.zds.ZdsTabLayout;
import com.zebra.zdsDemo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class IconGalleryFragment extends Fragment {

    private enum Style {
        ALL, ROUND, SHARP, OUTLINE
    }

    private static final String ICON_PREFIX = "ic_";

    private final List<Icon> allIcons = new ArrayList<>();
    private final List<Icon> visibleIcons = new ArrayList<>();

    private IconAdapter adapter;
    private TextView count;
    private View emptyState;
    private Style style = Style.ALL;
    private String query = "";

    public IconGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allIcons.addAll(loadIcons());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_icon_gallery, container, false);

        style = Style.ALL;
        query = "";

        count = root.findViewById(R.id.iconCount);
        emptyState = root.findViewById(R.id.iconEmptyState);

        adapter = new IconAdapter();
        RecyclerView grid = root.findViewById(R.id.iconGrid);
        grid.setAdapter(adapter);
        grid.setLayoutManager(new GridLayoutManager(getContext(), 4));
        grid.setHasFixedSize(true);

        ZdsSearchView search = root.findViewById(R.id.iconSearch);
        search.setOnQueryTextListener(new ZdsSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                query = text == null ? "" : text.trim().toLowerCase(Locale.US);
                applyFilters();
                return true;
            }
        });

        ZdsTabLayout tabs = root.findViewById(R.id.iconStyleTabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                style = Style.values()[tab.getPosition()];
                applyFilters();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        applyFilters();
        return root;
    }

    private List<Icon> loadIcons() {
        List<Icon> icons = new ArrayList<>();
        for (Field field : com.zebra.zds.R.drawable.class.getFields()) {
            String name = field.getName();
            if (!name.startsWith(ICON_PREFIX)) {
                continue;
            }
            try {
                icons.add(new Icon(name, field.getInt(null)));
            } catch (IllegalAccessException ignored) {
                // Skip drawables that cannot be read.
            }
        }
        Collections.sort(icons, (left, right) -> left.name.compareTo(right.name));
        return icons;
    }

    private void applyFilters() {
        visibleIcons.clear();
        for (Icon icon : allIcons) {
            if (matchesStyle(icon.name) && icon.name.contains(query)) {
                visibleIcons.add(icon);
            }
        }
        adapter.notifyDataSetChanged();
        count.setText(getResources().getQuantityString(R.plurals.icon_gallery_count,
                visibleIcons.size(), visibleIcons.size()));
        emptyState.setVisibility(visibleIcons.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private boolean matchesStyle(String name) {
        switch (style) {
            case ROUND:
                return name.endsWith("_round");
            case SHARP:
                return name.endsWith("_sharp");
            case OUTLINE:
                return name.contains("_outline");
            default:
                return true;
        }
    }

    private void copyToClipboard(View anchor, String name) {
        Context context = anchor.getContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(name, "@drawable/" + name));
        }
        Snackbar.make(anchor, getString(R.string.icon_gallery_copied, name), Snackbar.LENGTH_SHORT).show();
    }

    private static class Icon {
        final String name;
        final int resId;

        Icon(String name, int resId) {
            this.name = name;
            this.resId = resId;
        }
    }

    private class IconAdapter extends RecyclerView.Adapter<IconViewHolder> {

        @NonNull
        @Override
        public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_icon_gallery, parent, false);
            return new IconViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
            Icon icon = visibleIcons.get(position);
            holder.icon.setImageResource(icon.resId);
            holder.name.setText(icon.name);
            holder.itemView.setOnClickListener(v -> copyToClipboard(v, icon.name));
        }

        @Override
        public int getItemCount() {
            return visibleIcons.size();
        }
    }

    private static class IconViewHolder extends RecyclerView.ViewHolder {
        final AppCompatImageView icon;
        final TextView name;

        IconViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.iconName);
        }
    }
}
