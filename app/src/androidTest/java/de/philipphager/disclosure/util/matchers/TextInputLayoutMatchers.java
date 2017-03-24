package de.philipphager.disclosure.util.matchers;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TextInputLayoutMatchers {
  public static Matcher<View> hasTextInputLayoutHintText(final String expectedHintText) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof TextInputLayout)) {
          return false;
        }

        CharSequence hint = ((TextInputLayout) view).getHint();

        if (hint == null || hint.length() == 0) {
          return false;
        }

        String hintText = hint.toString();
        return expectedHintText.equals(hintText);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("Expected: TextInputLayout hint text to match '")
            .appendText(expectedHintText)
            .appendText("'");
      }
    };
  }

  public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof TextInputLayout)) {
          return false;
        }

        CharSequence error = ((TextInputLayout) view).getError();

        if (error == null || error.length() == 0) {
          return false;
        }

        String errorText = error.toString();
        return expectedErrorText.equals(errorText);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("Expected: TextInputLayout error text to match '")
            .appendText(expectedErrorText)
            .appendText("'");
      }
    };
  }

  public static Matcher<View> hasTextInputLayoutErrorText() {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof TextInputLayout)) {
          return false;
        }

        CharSequence error = ((TextInputLayout) view).getError();
        return error != null && error.length() > 0;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Expected: TextInputLayout to have error, but was null or empty'");
      }
    };
  }
}
