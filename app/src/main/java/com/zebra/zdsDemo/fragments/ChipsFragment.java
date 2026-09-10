package com.zebra.zdsDemo.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;
import com.zebra.zds.ZdsChip;
import com.zebra.zdsDemo.R;

import java.util.ArrayList;
import java.util.List;

public class ChipsFragment extends Fragment {

    public ChipsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chips, container, false);

        ChipGroup filterChipGroup = root.findViewById(R.id.filterChipGroup);
        MaterialTextView selection = root.findViewById(R.id.chipSelection);

        filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> showSelection(group, selection));
        showSelection(filterChipGroup, selection);

        return root;
    }

    private void showSelection(ChipGroup group, MaterialTextView selection) {
        List<String> labels = new ArrayList<>();
        for (int id : group.getCheckedChipIds()) {
            labels.add(((ZdsChip) group.findViewById(id)).getText().toString());
        }
        selection.setText(labels.isEmpty() ? "No filters" : "Filters: " + TextUtils.join(", ", labels));
    }
}
