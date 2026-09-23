package com.zebra.zdsDemo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.material.textfield.TextInputLayout;
import com.zebra.zds.ZdsTextInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.hamcrest.Matcher;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZdsTextInputTest {

    private ActivityScenario<ZdsHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ZdsScenarios.launchWith(R.layout.fragment_text_input);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void parsesMandatoryLabelAttributes() {
        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input);
            assertEquals(ZdsTextInput.LabelStyle.MANDATORY, input.getLabelStyle());
            assertEquals("Custom label", input.getLabelString());
            assertEquals("Custom label*", input.getLabelText().getText().toString());
        });
    }

    @Test
    public void parsesOptionalLabelAttributes() {
        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input_dense);
            assertEquals(ZdsTextInput.LabelStyle.OPTIONAL, input.getLabelStyle());
            assertEquals(
                    "Optional label " + activity.getString(com.zebra.zds.R.string.optional),
                    input.getLabelText().getText().toString());
        });
    }

    @Test
    public void parsesHintAndEndIconAttributes() {
        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input);
            assertEquals(activity.getString(R.string.app_name), input.getHintString());
            assertEquals(View.VISIBLE, input.getErrorText().getVisibility());
            assertEquals(View.VISIBLE, input.getInfoIcon().getVisibility());

            TextInputLayout layout = input.getTextInputLayout();
            assertEquals(TextInputLayout.END_ICON_CUSTOM, layout.getEndIconMode());
            assertNotNull(layout.getEndIconDrawable());
        });
    }

    @Test
    public void errorReplacesHintAndClearingRestoresIt() {
        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input);
            input.setError("Value is invalid");
        });

        onView(errorTextOf(R.id.zebra_text_input))
                .check(matches(allOf(isDisplayed(), withText("Value is invalid"))));

        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input);
            input.setError(null);
        });

        onView(errorTextOf(R.id.zebra_text_input)).check(matches(withText(hintText())));
    }

    @Test
    public void errorIsHiddenAgainWhenThereIsNoHint() {
        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input_sharp);
            input.setError("Value is invalid");
        });

        onView(errorTextOf(R.id.zebra_text_input_sharp)).check(matches(isDisplayed()));

        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input_sharp);
            input.setError(null);
            assertEquals(View.GONE, input.getErrorText().getVisibility());
            assertEquals(View.GONE, input.getInfoIcon().getVisibility());
        });

        onView(errorTextOf(R.id.zebra_text_input_sharp)).check(matches(not(isDisplayed())));
    }

    @Test
    public void typedTextIsReadableThroughTheComponent() {
        onView(editTextOf(R.id.zebra_text_input))
                .perform(clearText(), typeText("serial-1234"), closeSoftKeyboard());

        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input);
            assertEquals("serial-1234", input.getText().toString());
        });
    }

    @Test
    public void placeholderIsShownOnTheEditText() {
        scenario.onActivity(activity -> {
            ZdsTextInput input = activity.findViewById(R.id.zebra_text_input_sharp);
            input.setPlaceholder("Scan a barcode");
            assertEquals("Scan a barcode", input.getTextInputEditText().getHint().toString());
        });
    }

    private static String hintText() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getString(R.string.app_name);
    }

    private static Matcher<View> errorTextOf(int componentId) {
        return allOf(withId(com.zebra.zds.R.id.textError), isDescendantOfA(withId(componentId)));
    }

    private static Matcher<View> editTextOf(int componentId) {
        return allOf(withId(com.zebra.zds.R.id.textInputEditText), isDescendantOfA(withId(componentId)));
    }
}
