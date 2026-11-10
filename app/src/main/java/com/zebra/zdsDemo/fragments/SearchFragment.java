package com.zebra.zdsDemo.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zebra.zds.ZdsListItem;
import com.zebra.zds.ZdsSearchView;
import com.zebra.zds.ZdsSwitch;
import com.zebra.zdsDemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private TextView emptyState;
    private ComponentAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        emptyState = root.findViewById(R.id.search_empty_state);

        adapter = new ComponentAdapter(Arrays.asList(getResources().getStringArray(R.array.search_component_names)));
        RecyclerView results = root.findViewById(R.id.search_results);
        results.setLayoutManager(new LinearLayoutManager(requireContext()));
        results.setAdapter(adapter);

        ZdsSearchView searchView = root.findViewById(R.id.zebra_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                root.requestFocus();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        ZdsSwitch sharpSwitch = root.findViewById(R.id.search_sharp_switch);
        sharpSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                searchView.setStyle(isChecked ? ZdsSearchView.Style.SHARP : ZdsSearchView.Style.ROUND));

        return root;
    }

    private void filter(String query) {
        String trimmed = query.trim();
        adapter.filter(trimmed);

        boolean isEmpty = adapter.getItemCount() == 0;
        emptyState.setText(isEmpty ? getString(R.string.search_empty_state, trimmed) : null);
        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    static class ComponentAdapter extends RecyclerView.Adapter<ZdsListItem.ZdsViewHolder> {

        private final List<String> components;
        private final List<String> filtered;

        ComponentAdapter(List<String> components) {
            this.components = components;
            this.filtered = new ArrayList<>(components);
        }

        @SuppressLint("NotifyDataSetChanged")
        void filter(String query) {
            String needle = query.toLowerCase(Locale.ROOT);
            filtered.clear();
            for (String component : components) {
                if (component.toLowerCase(Locale.ROOT).contains(needle)) {
                    filtered.add(component);
                }
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ZdsListItem.ZdsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ZdsListItem.ZdsViewHolder(ZdsListItem.Companion.getListItem(viewGroup));
        }

        @Override
        public void onBindViewHolder(@NonNull ZdsListItem.ZdsViewHolder viewHolder, int position) {
            viewHolder.getContent().setText(filtered.get(position));
            viewHolder.getDescriptor().setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return filtered.size();
        }
    }
}
