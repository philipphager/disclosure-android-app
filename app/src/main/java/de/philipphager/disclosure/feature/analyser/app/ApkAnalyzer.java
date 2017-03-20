package de.philipphager.disclosure.feature.analyser.app;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class ApkAnalyzer {
  private static final int MIN_INDEX = 0;
  private final List<String> classes;
  private final Apk apk;

  public ApkAnalyzer(Apk apk) throws IOException {
    ensureNotNull(apk, "must provide apk");
    this.apk = apk;
    this.classes = apk.getUsedClasses()
        .toBlocking()
        .first();
  }

  public boolean usesLibrary(String packageName) {
    String currentThread = Thread.currentThread().getName();
    Timber.v("%s : Searching for package %s in app %s", currentThread, packageName, apk.getApp().label());

    int index = Collections.binarySearch(classes, packageName, (currentItem, key) -> {
      if (currentItem.startsWith(key)) {
        return 0;
      }
      return currentItem.compareTo(key);
    });
    return index >= MIN_INDEX;
  }
}
