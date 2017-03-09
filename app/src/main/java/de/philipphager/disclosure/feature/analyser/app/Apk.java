package de.philipphager.disclosure.feature.analyser.app;

import de.philipphager.disclosure.database.app.model.App;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.DexClass;
import rx.Observable;
import timber.log.Timber;

public class Apk {
  private static final int MIN_INDEX = 0;
  private final App app;
  private List<String> packageNames;

  public Apk(App app) throws IOException {
    this.app = app;
    load();
  }

  private void load() throws IOException {
    ApkParser apk = new ApkParser(app.sourceDir());
    DexClass[] dexClasses = apk.getDexClasses();

    packageNames = Observable.from(dexClasses)
        .map(DexClass::getPackageName)
        .distinct()
        .toSortedList()
        .toBlocking()
        .first();
  }

  public boolean containsPackage(String packageName) {
    String currentThread = Thread.currentThread().getName();
    Timber.d("%s : Searching for package %s in app %s", currentThread, packageName, app.label());

    int index = Collections.binarySearch(packageNames, packageName, (currentItem, key) -> {
      if(currentItem.startsWith(key)) {
        return 0;
      }
      return currentItem.compareTo(key);
    });
    return index >= MIN_INDEX;
  }
}
