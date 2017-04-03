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
  private final DexFile dexFile;

  public Apk(App app) throws IOException {
    this.app = app;
    this.dexFile = new DexFile(new File(app.sourceDir()));
  }

  public App getApp() {
    return app;
  }

  public Observable<List<String>> getUsedClasses() {
    List<String> classNames = new ArrayList<>();
    classNames.addAll(Collections.list(dexFile.entries()));

    return Observable.from(classNames)
        .distinct()
        .toSortedList();
  }
}
