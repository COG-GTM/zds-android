package com.zebra.zdsDemo.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
        return inflater.inflate(R.layout.fragment_chips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChipGroup selectableGroup = view.findViewById(R.id.selectableChipGroup);
        TextView selectableResult = view.findViewById(R.id.selectableResult);
        selectableGroup.setOnCheckedStateChangeListener((group, checkedIds) ->
                selectableResult.setText(getString(R.string.chips_selected, checkedLabels(group, checkedIds))));
        selectableResult.setText(getString(R.string.chips_selected,
                checkedLabels(selectableGroup, selectableGroup.getCheckedChipIds())));

        ChipGroup filterGroup = view.findViewById(R.id.filterChipGroup);
        TextView filterResult = view.findViewById(R.id.filterResult);
        filterGroup.setOnCheckedStateChangeListener((group, checkedIds) ->
                filterResult.setText(getString(R.string.chips_active_filters, checkedLabels(group, checkedIds))));
        filterResult.setText(getString(R.string.chips_active_filters,
                checkedLabels(filterGroup, filterGroup.getCheckedChipIds())));
    }

    private String checkedLabels(ChipGroup group, List<Integer> checkedIds) {
        List<CharSequence> labels = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof Chip && checkedIds.contains(child.getId())) {
                labels.add(((Chip) child).getText());
            }
        }
        return labels.isEmpty() ? getString(R.string.chips_none) : TextUtils.join(", ", labels);
    }
}
