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
import com.google.android.material.snackbar.Snackbar;
import com.zebra.zds.ZdsChip;
import com.zebra.zdsDemo.R;

import java.util.ArrayList;
import java.util.List;

public class ChipsFragment extends Fragment {

    private static final String[] TAGS = {"Aisle 4", "Pallet 12", "Fragile", "Priority"};

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

        view.findViewById(R.id.assistChip).setOnClickListener(v ->
                Snackbar.make(v, "Scan barcode tapped", Snackbar.LENGTH_SHORT).show());

        ChipGroup filterGroup = view.findViewById(R.id.filterChipGroup);
        TextView filterSelection = view.findViewById(R.id.filterSelection);
        filterGroup.setOnCheckedStateChangeListener((group, checkedIds) ->
                updateFilterSelection(group, checkedIds, filterSelection));
        updateFilterSelection(filterGroup, filterGroup.getCheckedChipIds(), filterSelection);

        ChipGroup inputGroup = view.findViewById(R.id.inputChipGroup);
        view.findViewById(R.id.inputChipReset).setOnClickListener(v -> populateInputChips(inputGroup));
        populateInputChips(inputGroup);
    }

    private void updateFilterSelection(ChipGroup group, List<Integer> checkedIds, TextView label) {
        List<String> names = new ArrayList<>();
        for (int id : checkedIds) {
            Chip chip = group.findViewById(id);
            if (chip.isEnabled()) {
                names.add(chip.getText().toString());
            }
        }
        label.setText(names.isEmpty() ? "No filters selected" : "Selected: " + TextUtils.join(", ", names));
    }

    private void populateInputChips(ChipGroup group) {
        group.removeAllViews();
        for (String tag : TAGS) {
            ZdsChip chip = (ZdsChip) LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_input_chip, group, false);
            chip.setText(tag);
            chip.setOnRemoveListener(group::removeView);
            group.addView(chip);
        }
    }
}
