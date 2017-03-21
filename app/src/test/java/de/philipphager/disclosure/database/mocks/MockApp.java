package de.philipphager.disclosure.database.mocks;

import android.content.ContentValues;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppInfo;

public final class MockApp {
  public static final App TEST = App.builder()
      .id(1L)
      .label("facebook")
      .packageName("com.facebook.android")
      .process("com.facebook")
      .targetSdk(21)
      .flags(0)
      .sourceDir("/")
      .build();
  public static final App TEST2 = App.builder()
      .id(2L)
      .label("instagram")
      .packageName("com.instagram.android")
      .process("com.instagram")
      .targetSdk(21)
      .flags(0)
      .sourceDir("/")
      .build();
  public static final AppInfo TEST_INFO = AppInfo.create(TEST.packageName(), 1001);
  public static final AppInfo TEST2_INFO = AppInfo.create(TEST2.packageName(), 2001);

  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockApp() {
    //No instances of helper classes.
  }

  public static ContentValues getTestContentValues(App app) {
    ContentValues appContentValues = new ContentValues();
    appContentValues.put("id", app.id());
    appContentValues.put("label", app.label());
    appContentValues.put("packageName", app.packageName());
    appContentValues.put("sourceDir", app.sourceDir());
    appContentValues.put("process", app.process());
    appContentValues.put("flags", app.flags());
    return appContentValues;
  }
}
