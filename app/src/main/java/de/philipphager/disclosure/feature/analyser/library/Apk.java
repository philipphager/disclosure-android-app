package de.philipphager.disclosure.feature.analyser.library;

import de.philipphager.disclosure.database.app.model.App;
import java.io.IOException;
import java.util.Arrays;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.DexClass;
import timber.log.Timber;

public class Apk {
  private static final int MIN_INDEX = 0;
  private final App app;
  private String[] packageNames;

  public Apk(App app) throws IOException {
    this.app = app;
    load();
  }

  private void load() throws IOException {
    ApkParser apk = new ApkParser(app.sourceDir());
    DexClass[] dexClasses = apk.getDexClasses();
    packageNames = new String[dexClasses.length];

    for (int i = 0; i < dexClasses.length; i++) {
      packageNames[i] = dexClasses[i].getPackageName();
    }
  }

  public boolean containsPackage(String packageName) {
    String currentThread = Thread.currentThread().getName();
    Timber.d("%s : Searching for package %s in app %s", currentThread, packageName, app.label());
    int index = Arrays.binarySearch(packageNames, packageName);
    return index >= MIN_INDEX;
  }
}
