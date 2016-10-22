package de.philipphager.disclosure.database.app;

import android.content.pm.PackageInfo;

public final class MockPackage {
  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockPackage() {
    //No instances of helper classes.
  }

  public static PackageInfo test() {
    PackageInfo packageInfo = new PackageInfo();
    packageInfo.packageName = MockApp.TEST.packageName();
    packageInfo.versionCode = MockApp.TEST_INFO.versionCode();
    return packageInfo;
  }

  public static PackageInfo test2() {
    PackageInfo packageInfo = new PackageInfo();
    packageInfo.packageName = MockApp.TEST2.packageName();
    packageInfo.versionCode = MockApp.TEST2_INFO.versionCode();
    return packageInfo;
  }
}
