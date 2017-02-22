package de.philipphager.disclosure.feature.navigation;

import android.app.Activity;
import android.content.Intent;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockLibrary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class NavigatorShould {
  @Mock protected Activity activity;
  @InjectMocks protected Navigator navigator;

  @Before public void setUp() {
    navigator.setActivity(activity);
  }

  @Test public void navigateToHomeActivity() {
    navigator.toHome();

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getComponent().getClassName())
        .isEqualTo("de.philipphager.disclosure.feature.home.HomeActivity");
  }

  @Test public void navigateToDetailActivity() {
    App mockApp = MockApp.TEST;
    navigator.toAppDetail(mockApp);

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getComponent().getClassName())
        .isEqualTo("de.philipphager.disclosure.feature.app.detail.DetailActivity");
  }

  @Test public void navigateToDetailActivityPassesAppParcelable() {
    App mockApp = MockApp.TEST;
    navigator.toAppDetail(mockApp);

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getComponent().getClassName())
        .isEqualTo("de.philipphager.disclosure.feature.app.detail.DetailActivity");
  }

  @Test public void navigateToLibraryOverviewActivity() {
    Library mockLibrary = MockLibrary.TEST;
    navigator.toLibraryDetail(mockLibrary);

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getComponent().getClassName())
        .isEqualTo("de.philipphager.disclosure.feature.library.detail.LibraryDetailActivity");
  }

  @Test public void navigateToLibraryOverviewActivityPassesLibraryParcelable() {
    Library mockLibrary = MockLibrary.TEST;
    navigator.toLibraryDetail(mockLibrary);

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.<App>getParcelableExtra("EXTRA_LIBRARY"))
        .isEqualTo(mockLibrary);
  }

  @Test public void navigateToSystemSettingsForApp() {
    String mockPackageName = MockApp.TEST.packageName();
    navigator.toAppSystemSettings(mockPackageName);

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getAction())
        .isEqualTo("android.settings.APPLICATION_DETAILS_SETTINGS");
  }

  @Test public void navigateToSystemSettingsForAppPassesPackageNameOfApp() {
    String mockPackageName = MockApp.TEST.packageName();
    navigator.toAppSystemSettings(mockPackageName);

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getData().toString())
        .isEqualTo("package:com.facebook.android");
  }

  @Test public void navigateToSystemUpdates() {
    navigator.toSystemUpdates();

    ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
    verify(activity).startActivity(intentCaptor.capture());

    Intent intent = intentCaptor.getValue();
    assertThat(intent.getAction())
        .isEqualTo("android.settings.SYSTEM_UPDATE_SETTINGS");
  }
}
