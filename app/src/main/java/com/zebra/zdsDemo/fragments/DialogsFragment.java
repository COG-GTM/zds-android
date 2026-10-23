package com.zebra.zdsDemo.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zebra.zdsDemo.R;
import com.zebra.zds.ZdsDialog;
import com.zebra.zds.ZdsSwitch;

public class DialogsFragment extends Fragment {

    private static final String KEY_SHOWN_DIALOG = "shownDialog";
    private static final String KEY_DIALOG_STATE = "dialogState";
    private static final int DIALOG_NONE = 0;
    private static final int DIALOG_DEFAULT = 1;
    private static final int DIALOG_LONG_CONTENT = 2;

    private ZdsSwitch sharpStyle;
    private ZdsSwitch titleImage;
    private RadioGroup gravityRadioGroup;
    private RadioGroup actionsRadioGroup;

    private ZdsDialog dialog;
    private int shownDialog = DIALOG_NONE;

    public DialogsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialogs, container, false);

        sharpStyle = root.findViewById(R.id.sharpStyle);
        titleImage = root.findViewById(R.id.titleImage);
        gravityRadioGroup = root.findViewById(R.id.gravityRadioGroup);
        actionsRadioGroup = root.findViewById(R.id.actionsRadioGroup);

        root.findViewById(R.id.showDialogButton).setOnClickListener(v -> showDialog(DIALOG_DEFAULT));
        root.findViewById(R.id.showLongContentDialogButton).setOnClickListener(v -> showDialog(DIALOG_LONG_CONTENT));

        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Re-show the dialog after a configuration change (e.g. rotation), once the
        // style switches and radio groups have restored their state.
        if (savedInstanceState != null) {
            showDialog(savedInstanceState.getInt(KEY_SHOWN_DIALOG, DIALOG_NONE));
            Bundle dialogState = savedInstanceState.getBundle(KEY_DIALOG_STATE);
            if (dialog != null && dialogState != null) {
                // Restores view state such as the content scroll position.
                dialog.onRestoreInstanceState(dialogState);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SHOWN_DIALOG, shownDialog);
        if (dialog != null) {
            outState.putBundle(KEY_DIALOG_STATE, dialog.onSaveInstanceState());
        }
    }

    @Override
    public void onDestroyView() {
        if (dialog != null) {
            // Dismiss without clearing shownDialog so it is restored after recreation.
            dialog.setOnDismissListener(null);
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroyView();
    }

    @SuppressLint("NonConstantResourceId")
    private void showDialog(int type) {
        if (type == DIALOG_NONE) {
            return;
        }

        ZdsDialog.Builder builder = new ZdsDialog.Builder(requireContext());
        if (titleImage.isChecked()) {
            builder.setIcon(R.drawable.ic_warning_round);
        }
        if (gravityRadioGroup.getCheckedRadioButtonId() == R.id.gravityLeft) {
            builder.setTitleGravity(Gravity.START);
        } else {
            builder.setTitleGravity(Gravity.CENTER);
        }
        View.OnClickListener listener = v -> {

        };

        if (type == DIALOG_LONG_CONTENT) {
            builder.setTitle(getString(R.string.dialog_long_content_title))
                    .setContent(getString(R.string.dialog_long_content))
                    .setPositiveAction("Accept", listener)
                    .setNegativeAction("Decline", listener)
                    .setNeutralAction("Learn More", listener);
        } else {
            builder.setTitle("Dialog Title").setContent("Lorem ipsum dolor sit amet, conse ctetur adipiscing elit, sed do eiusm od tempor incididunt ut labore et do lore magna aliqua.");
            switch (actionsRadioGroup.getCheckedRadioButtonId()) {
                case R.id.oneAction:
                    builder.setPositiveAction("Confirm", listener);
                    break;
                case R.id.twoActions:
                    builder.setPositiveAction("Confirm", listener);
                    builder.setNegativeAction("Cancel", listener);
                    break;
                case R.id.threeActions:
                    builder.setPositiveAction("Confirm", listener);
                    builder.setNegativeAction("Cancel", listener);
                    builder.setNeutralAction("Learn More", listener);
                    break;
            }
        }

        builder.setIsSharp(sharpStyle.isChecked());

        dialog = builder.build();
        dialog.setOnDismissListener(d -> {
            dialog = null;
            shownDialog = DIALOG_NONE;
        });
        shownDialog = type;
        dialog.show();
    }
}
