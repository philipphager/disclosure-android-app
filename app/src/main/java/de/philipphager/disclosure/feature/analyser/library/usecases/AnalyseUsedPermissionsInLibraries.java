package de.philipphager.disclosure.feature.analyser.library.usecases;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.library.Apk;
import de.philipphager.disclosure.feature.analyser.library.LibraryParser;
import de.philipphager.disclosure.feature.analyser.library.Method;
import de.philipphager.disclosure.service.LibraryService;
import de.philipphager.disclosure.util.device.FileUtils;
import de.philipphager.disclosure.util.device.StorageProvider;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AnalyseUsedPermissionsInLibraries {
  /**
   * "com/mixpanel/android/", "com/google/android/gms/", "com/adjust/sdk/",
   "com/crashlytics/android/", "com/facebook/", "com/kahuna/sdk/", "com/usebutton/sdk/"
   */

  private final LibraryService libraryService;
  private final StorageProvider storageProvider;

  @Inject public AnalyseUsedPermissionsInLibraries(LibraryService libraryService,
      StorageProvider storageProvider) {
    this.libraryService = libraryService;
    this.storageProvider = storageProvider;
  }

  public Observable<List<Method>> analyse(App app) {
    Timber.d("analysing %s", app);

    File outputDir = getOutputDirForApp(app);

    return decompileApk(app, outputDir)
        .flatMap(apk -> libraryService.byApp(app)
            .first()
            .flatMap(Observable::from)
            .flatMap(library -> Observable.just(library)
                .subscribeOn(Schedulers.computation())
                .map(lib -> outputDir + File.separator + lib.sourceDir())
                .map(File::new)
                .filter(File::exists)
                .map(LibraryParser::new)
                .flatMap(LibraryParser::findMethodInvocations)
                .doOnNext(methods -> Timber.d("%s method invocations in library %s", methods.size(), library.title())))
        ).doOnTerminate(() -> {
          Timber.d("Delete %s compilation files in %s", app.packageName(), outputDir.getPath());
          FileUtils.delete(outputDir);
        });
  }

  private Observable<Apk> decompileApk(App app, File outputDir) {
    return Observable.fromCallable(() -> {
      Apk apk = new Apk(app);
      apk.decompileTo(outputDir);
      return apk;
    });
  }

  private File getOutputDirForApp(App app) {
    File rootDir = storageProvider.getRootDir();
    return new File(rootDir.getPath() + File.separator + app.packageName());
  }
}
