package de.philipphager.disclosure.database.app;

import android.content.pm.PackageInfo;

public class MockPackage {
  public static final PackageInfo TEST = new PackageInfo() {{
    packageName = MockApp.TEST.packageName();
    versionCode = MockApp.TEST_INFO.versionCode();
  }};

  public static PackageInfo TEST2 = new PackageInfo() {{
    packageName = MockApp.TEST2.packageName();
    versionCode = MockApp.TEST2_INFO.versionCode();
  }};
}
