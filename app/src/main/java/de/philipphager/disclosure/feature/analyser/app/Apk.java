package de.philipphager.disclosure.feature.analyser.app;

import dalvik.system.DexFile;
import de.philipphager.disclosure.database.app.model.App;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Observable;

public class Apk {
  private final App app;

  public Apk(App app) throws IOException {
    this.app = app;
  }

  public App getApp() {
    return app;
  }

  public Observable<List<String>> getUsedClasses() throws IOException {
    File file = new File(app.sourceDir());
    List<String> classNames = new ArrayList<>();

    if (file.exists()) {
      DexFile dexFile = new DexFile(app.sourceDir());
      classNames.addAll(Collections.list(dexFile.entries()));
    }

    return Observable.from(classNames)
        .distinct()
        .toSortedList();
  }
}
