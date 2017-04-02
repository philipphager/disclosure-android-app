package de.philipphager.disclosure;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import de.philipphager.disclosure.feature.library.create.CreateLibraryActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.philipphager.disclosure.util.matchers.TextInputLayoutMatchers.hasTextInputLayoutErrorText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateLibraryActivityShould {
  @Rule public ActivityTestRule<CreateLibraryActivity> activityRule =
      new ActivityTestRule<>(CreateLibraryActivity.class);
  private String titleErrorHint;
  private String packageNameErrorHint;
  private String websiteUrlErrorHint;
  private String duplicateLibraryHint;

  @Before public void setUp() throws Exception {
    titleErrorHint =
        activityRule.getActivity().getString(R.string.activity_library_create_title_invalid);
    packageNameErrorHint =
        activityRule.getActivity().getString(R.string.activity_library_create_package_name_invalid);
    websiteUrlErrorHint =
        activityRule.getActivity().getString(R.string.activity_library_create_url_invalid);
    duplicateLibraryHint =
        activityRule.getActivity().getString(R.string.activity_library_create_error_duplicate);
  }

  @Test public void closeActivityOnClickCancel() {
    onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    assertTrue(activityRule.getActivity().isFinishing());
  }

  @Test public void showErrorsWhenNotProvidingTitleAndPackageName() {
    onView(withId(R.id.ic_done))
        .perform(click());

    onView(withId(R.id.library_layout_title))
        .check(matches(hasTextInputLayoutErrorText(titleErrorHint)));

    onView(withId(R.id.library_layout_package_name))
        .check(matches(hasTextInputLayoutErrorText(packageNameErrorHint)));
  }

  @Test public void showTitleErrorWhenNotProvidingTitle() {
    onView(withId(R.id.library_package_name))
        .perform(typeText("com.google.analytics"));

    onView(withId(R.id.ic_done))
        .perform(click());

    onView(withId(R.id.library_layout_title))
        .check(matches(hasTextInputLayoutErrorText(titleErrorHint)));

    onView(withId(R.id.library_layout_package_name))
        .check(matches(not(hasTextInputLayoutErrorText())));
  }

  @Test public void showPackageNameErrorWhenNotProvidingPackageName() {
    onView(withId(R.id.library_title))
        .perform(typeText("Google Analytics"));

    onView(withId(R.id.ic_done))
        .perform(click());

    onView(withId(R.id.library_layout_package_name))
        .check(matches(hasTextInputLayoutErrorText(packageNameErrorHint)));

    onView(withId(R.id.library_layout_title))
        .check(matches(not(hasTextInputLayoutErrorText())));
  }

  @Test public void showPackageNameErrorWhenProvidingAnInvalidPackageName() {
    onView(withId(R.id.library_title))
        .perform(typeText("Google Analytics"));

    onView(withId(R.id.library_package_name))
        .perform(typeText("not a valid packagename"));

    onView(withId(R.id.ic_done))
        .perform(click());

    onView(withId(R.id.library_layout_package_name))
        .check(matches(hasTextInputLayoutErrorText(packageNameErrorHint)));
  }

  @Test public void createLibraryWhenTitleAndPackageNameAreProvidedAndValid() {
    onView(withId(R.id.library_title))
        .perform(typeText("Google Analytics"));

    onView(withId(R.id.library_package_name))
        .perform(typeText("com.google.analytics"));

    onView(withId(R.id.ic_done))
        .perform(click());

    assertTrue(activityRule.getActivity().isFinishing());
  }

  @Test public void showWebsiteUrlErrorWhenProvidedUrlIsInvalid() {
    onView(withId(R.id.library_title))
        .perform(typeText("Google Analytics"));

    onView(withId(R.id.library_package_name))
        .perform(typeText("com.google.analytics"));

    onView(withId(R.id.library_website))
        .perform(typeText("not a a valid url"));

    onView(withId(R.id.ic_done))
        .perform(click());

    onView(withId(R.id.library_layout_website))
        .check(matches(hasTextInputLayoutErrorText(websiteUrlErrorHint)));
  }

  @Test public void createLibraryWhenTitleAndPackageNameAndWebsiteUrlAreProvidedAndValid() {
    onView(withId(R.id.library_title))
        .perform(typeText("Google Analytics 2"));

    onView(withId(R.id.library_package_name))
        .perform(typeText("com.google.analytics2"));

    onView(withId(R.id.library_website))
        .perform(typeText("https://wwww.valid.url.com"));

    onView(withId(R.id.ic_done))
        .perform(click());

    assertTrue(activityRule.getActivity().isFinishing());
  }

  @Test public void showDuplicateLibraryWarningIfLibraryWithPackageNameAlreadyExits() {
    onView(withId(R.id.library_title))
        .perform(typeText("Google Analytics 3"));

    onView(withId(R.id.library_package_name))
        .perform(typeText("com.google.analytics"));

    onView(withId(R.id.ic_done))
        .perform(click());

    // Check for toast message
    onView(withText(duplicateLibraryHint))
        .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
        .check(matches(isDisplayed()));
  }
}
