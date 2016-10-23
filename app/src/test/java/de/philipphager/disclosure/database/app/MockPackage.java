package de.philipphager.disclosure.database.app;

import android.content.pm.PackageInfo;

public final class MockPackage {
  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockPackage() {
    //No instances of helper classes.
  }

  @SuppressWarnings("PMD.NonStaticInitializer")
  public static final PackageInfo TEST = new PackageInfo() {{
    packageName = MockApp.TEST.packageName();
    versionCode = MockApp.TEST_INFO.versionCode();
  }};

  @SuppressWarnings("PMD.NonStaticInitializer")
  public static final PackageInfo TEST2 = new PackageInfo() {{
    packageName = MockApp.TEST2.packageName();
    versionCode = MockApp.TEST2_INFO.versionCode();
  }};
}
