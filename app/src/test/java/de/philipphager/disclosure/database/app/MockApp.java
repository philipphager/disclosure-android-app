package de.philipphager.disclosure.database.app;

import android.content.ContentValues;
import de.philipphager.disclosure.database.app.model.App;

public class MockApp {
  public static final App TEST = App.builder()
      .id(1L)
      .label("facebook")
      .packageName("com.facebook.android")
      .process("com.facebook")
      .flags(0)
      .sourceDir("/")
      .build();

  public static final App.Info TEST_INFO = App.Info.create(TEST.packageName(), 1001);

  public static final App TEST2 = App.builder()
      .id(2L)
      .label("instagram")
      .packageName("com.instagram.android")
      .process("com.instagram")
      .flags(0)
      .sourceDir("/")
      .build();

  public static final App.Info TEST2_INFO = App.Info.create(TEST2.packageName(), 2001);

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
