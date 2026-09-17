package com.zebra.zdsDemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.zebra.zds.ZdsButton;
import com.zebra.zds.ZdsStepper;
import com.zebra.zdsDemo.R;

public class StepperFragment extends Fragment {

    ZdsStepper stepper;
    ZdsStepper stepperSharp;
    ZdsStepper stepperCompact;
    AppCompatTextView stepTitle;
    AppCompatTextView stepDescription;
    ZdsButton back;
    ZdsButton next;

    public StepperFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stepper, container, false);

        stepper = root.findViewById(R.id.stepper);
        stepperSharp = root.findViewById(R.id.stepperSharp);
        stepperCompact = root.findViewById(R.id.stepperCompact);
        stepTitle = root.findViewById(R.id.stepTitle);
        stepDescription = root.findViewById(R.id.stepDescription);
        back = root.findViewById(R.id.back);
        next = root.findViewById(R.id.next);

        stepper.setOnStepChangeListener((s, step) -> showStep(step, true));
        stepper.setOnStepClickListener((s, step) -> s.setCurrentStep(step, true));

        back.setOnClickListener(v -> stepper.previous());
        next.setOnClickListener(v -> {
            if (!stepper.next()) {
                stepper.setCurrentStep(0, true);
            }
        });

        showStep(stepper.getCurrentStep(), false);
        return root;
    }

    private void showStep(int step, boolean animate) {
        String[] descriptions = getResources().getStringArray(R.array.checkout_step_descriptions);
        stepTitle.setText(stepper.getSteps().get(step));
        stepDescription.setText(descriptions[step]);
        back.setEnabled(step > 0);
        next.setText(stepper.isLastStep() ? "Finish" : "Next");
        stepperSharp.setCurrentStep(step, animate);
        stepperCompact.setCurrentStep(step, animate);
    }
}
