package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.View;
import android.widget.ArrayAdapter;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.zebra.zds.ZdsSelectInput;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsSelectInputTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_select_input);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void inflatesWithoutHintAndKeepsHelperTextHidden() {
        scenario.onActivity(activity -> {
            ZdsSelectInput input = activity.findViewById(R.id.zebra_select_input);
            assertNotNull(input.getTextInputLayout());
            assertNotNull(input.getTextInputEditText());
            assertEquals(View.GONE, input.getErrorText().getVisibility());
            assertEquals(View.GONE, input.getInfoIcon().getVisibility());
        });
    }

    @Test
    public void hintIsShownAsHelperText() {
        scenario.onActivity(activity -> {
            ZdsSelectInput input = activity.findViewById(R.id.zebra_select_input);
            input.setHint("Pick a printer");
        });

        onView(errorTextOf(R.id.zebra_select_input))
                .check(matches(allOf(isDisplayed(), withText("Pick a printer"))));
    }

    @Test
    public void errorOverridesHintAndIsClearedBackToIt() {
        scenario.onActivity(activity -> {
            ZdsSelectInput input = activity.findViewById(R.id.zebra_select_input);
            input.setHint("Pick a printer");
            input.setError("Selection required");
        });

        onView(errorTextOf(R.id.zebra_select_input))
                .check(matches(allOf(isDisplayed(), withText("Selection required"))));

        scenario.onActivity(activity -> {
            ZdsSelectInput input = activity.findViewById(R.id.zebra_select_input);
            input.setError(null);
        });

        onView(errorTextOf(R.id.zebra_select_input)).check(matches(withText("Pick a printer")));
    }

    @Test
    public void dropdownSelectionUpdatesTheInputText() {
        scenario.onActivity(activity -> {
            ZdsSelectInput input = activity.findViewById(R.id.zebra_select_input_sharp);
            input.getTextInputEditText().setAdapter(new ArrayAdapter<>(
                    activity,
                    android.R.layout.simple_list_item_1,
                    new String[]{"Printer A", "Printer B"}));
        });

        onView(editTextOf(R.id.zebra_select_input_sharp)).perform(click());
        onView(withText("Printer B")).inRoot(isPlatformPopup()).perform(click());

        scenario.onActivity(activity -> {
            ZdsSelectInput input = activity.findViewById(R.id.zebra_select_input_sharp);
            assertEquals("Printer B", input.getText().toString());
        });
    }

    private static Matcher<View> errorTextOf(int componentId) {
        return allOf(withId(com.zebra.zds.R.id.textError), isDescendantOfA(withId(componentId)));
    }

    private static Matcher<View> editTextOf(int componentId) {
        return allOf(withId(com.zebra.zds.R.id.textInputEditText), isDescendantOfA(withId(componentId)));
    }
}
