package de.philipphager.disclosure.database.mocks;

import android.content.pm.ApplicationInfo;

public final class MockApplicationInfo {
  @SuppressWarnings("PMD.NonStaticInitializer")
  public static final ApplicationInfo TEST2 = new ApplicationInfo() {{
    packageName = MockApp.TEST2.packageName();
    processName = MockApp.TEST2.process();
    targetSdkVersion = MockApp.TEST2.targetSdk();
    flags = MockApp.TEST2.flags();
    sourceDir = MockApp.TEST2.sourceDir();
    labelRes = 0;
  }};

  private MockApplicationInfo() {
  }
}
