package com.zebra.zdsDemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.zebra.zds.ZdsChip;
import com.zebra.zdsDemo.R;

import java.util.ArrayList;
import java.util.List;

public class ChipsFragment extends Fragment {

    private final List<View> removedInputChips = new ArrayList<>();

    public ChipsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chips, container, false);

        ChipGroup assistChips = root.findViewById(R.id.assistChips);
        for (int i = 0; i < assistChips.getChildCount(); i++) {
            Chip chip = (Chip) assistChips.getChildAt(i);
            chip.setOnClickListener(v -> Snackbar.make(root, chip.getText() + " tapped", Snackbar.LENGTH_SHORT).show());
        }

        ChipGroup filterChips = root.findViewById(R.id.filterChips);
        TextView filterSummary = root.findViewById(R.id.filterSummary);
        filterChips.setOnCheckedStateChangeListener((group, checkedIds) -> updateFilterSummary(group, filterSummary));
        updateFilterSummary(filterChips, filterSummary);

        ChipGroup inputChips = root.findViewById(R.id.inputChips);
        for (int i = 0; i < inputChips.getChildCount(); i++) {
            ZdsChip chip = (ZdsChip) inputChips.getChildAt(i);
            chip.setOnCloseIconClickListener(v -> {
                inputChips.removeView(chip);
                removedInputChips.add(chip);
            });
        }

        root.findViewById(R.id.resetInputChips).setOnClickListener(v -> {
            for (View chip : removedInputChips) {
                inputChips.addView(chip);
            }
            removedInputChips.clear();
        });

        return root;
    }

    private void updateFilterSummary(ChipGroup group, TextView summary) {
        List<CharSequence> selected = new ArrayList<>();
        for (int id : group.getCheckedChipIds()) {
            selected.add(((Chip) group.findViewById(id)).getText());
        }
        summary.setText(selected.isEmpty()
                ? "Selected: none"
                : "Selected: " + String.join(", ", selected));
    }
}
