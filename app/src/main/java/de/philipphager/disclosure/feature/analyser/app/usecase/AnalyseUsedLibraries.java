package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.analyser.app.Apk;
import de.philipphager.disclosure.feature.analyser.app.ApkAnalyzer;
import de.philipphager.disclosure.service.LibraryService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AnalyseUsedLibraries {
  private final LibraryService libraryService;

  @Inject public AnalyseUsedLibraries(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  public Observable<List<Library>> analyse(App app) {
    Timber.d("analysing %s", app);

    List<Library> knownLibraries = libraryService.byApp(app)
        .toBlocking()
        .first();

    return loadApkAnalyzer(app)
        .flatMap(this::findLibraries)
        .flatMap(Observable::from)
        .filter(library -> !knownLibraries.contains(library))
        .toList()
        .doOnNext(libraries -> libraryService.insertForApp(app, libraries));
  }

  private Observable<ApkAnalyzer> loadApkAnalyzer(App app) {
    return Observable.fromCallable(() -> {
      Apk apk = new Apk(app);
      return new ApkAnalyzer(apk);
    });
  }

  private Observable<List<Library>> findLibraries(ApkAnalyzer apkAnalyzer) {
    return libraryService.all()
        .first()
        .flatMap(Observable::from)
        .flatMap(library -> Observable.just(library)
            .subscribeOn(Schedulers.computation())
            .filter(lib -> apkAnalyzer.usesLibrary(lib.packageName()))
        ).toList();
  }
}
