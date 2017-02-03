package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.app.Apk;
import java.io.File;
import javax.inject.Inject;
import rx.Observable;

public class DecompileApk {
  @SuppressWarnings("PMD.UnnecessaryConstructor") @Inject public DecompileApk() {
    // Needed for dagger injection.
  }

  public Observable<File> run(App app, File outputDir) {
    return Observable.fromCallable(() -> {
      Apk apk = new Apk(app);
      apk.decompileTo(outputDir);
      return outputDir;
    });
  }
}
