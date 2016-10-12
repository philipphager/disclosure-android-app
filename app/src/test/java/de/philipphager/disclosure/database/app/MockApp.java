package de.philipphager.disclosure.database.app;

import android.content.ContentValues;
import de.philipphager.disclosure.database.app.model.App;

public class MockApp {
  public static App TEST_APP = App.builder()
      .id(1L)
      .label("test-app")
      .packageName("test-package")
      .sourceDir("/")
      .process("test-process")
      .flags(0)
      .build();

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
