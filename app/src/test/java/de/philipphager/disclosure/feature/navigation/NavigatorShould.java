package de.philipphager.disclosure.feature.navigation;

import android.app.Activity;
import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class NavigatorShould {
  @Mock protected Activity activity;
  @InjectMocks protected Navigator navigator;

  @Before public void setUp() {
    navigator.setActivity(activity);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void navigateToHomeActivity() {
    navigator.toHome();

    verify(activity).startActivity(any(Intent.class));
  }
}
