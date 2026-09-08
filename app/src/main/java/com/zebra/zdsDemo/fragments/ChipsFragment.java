package com.zebra.zdsDemo.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
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
        return inflater.inflate(R.layout.fragment_chips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChipGroup selectableGroup = view.findViewById(R.id.selectableGroup);
        TextView selectableResult = view.findViewById(R.id.selectableResult);
        selectableGroup.setOnCheckedStateChangeListener((group, checkedIds) -> updateSelectable(group, selectableResult));
        updateSelectable(selectableGroup, selectableResult);

        ChipGroup filterGroup = view.findViewById(R.id.filterGroup);
        TextView filterResult = view.findViewById(R.id.filterResult);
        filterGroup.setOnCheckedStateChangeListener((group, checkedIds) -> updateFilters(group, filterResult));
        for (int i = 0; i < filterGroup.getChildCount(); i++) {
            ZdsChip chip = (ZdsChip) filterGroup.getChildAt(i);
            chip.setOnCloseIconClickListener(v -> {
                chip.setChecked(false);
                chip.setVisibility(View.GONE);
                updateFilters(filterGroup, filterResult);
            });
        }
        updateFilters(filterGroup, filterResult);

        view.findViewById(R.id.resetFilters).setOnClickListener(v -> {
            for (int i = 0; i < filterGroup.getChildCount(); i++) {
                ZdsChip chip = (ZdsChip) filterGroup.getChildAt(i);
                chip.setVisibility(View.VISIBLE);
                chip.setChecked(false);
            }
            updateFilters(filterGroup, filterResult);
        });
    }

    private void updateSelectable(ChipGroup group, TextView result) {
        ZdsChip checked = group.findViewById(group.getCheckedChipId());
        result.setText(checked == null ? "Selected: none" : "Selected: " + checked.getText());
    }

    private void updateFilters(ChipGroup group, TextView result) {
        List<CharSequence> active = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            ZdsChip chip = (ZdsChip) group.getChildAt(i);
            if (chip.isChecked() && chip.getVisibility() == View.VISIBLE) {
                active.add(chip.getText());
            }
        }
        result.setText(active.isEmpty() ? "Active filters: none" : "Active filters: " + TextUtils.join(", ", active));
    }
}
