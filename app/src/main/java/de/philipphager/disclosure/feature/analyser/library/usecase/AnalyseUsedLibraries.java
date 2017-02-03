package de.philipphager.disclosure.feature.analyser.library.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.analyser.app.Apk;
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

    return loadApk(app)
        .flatMap(this::findLibraries)
        .doOnNext(libraries -> libraryService.insertForApp(app, libraries));
  }

  private Observable<Apk> loadApk(App app) {
    return Observable.fromCallable(() -> new Apk(app));
  }

  private Observable<List<Library>> findLibraries(Apk apk) {
    return libraryService.all()
        .first()
        .flatMap(Observable::from)
        .flatMap(library -> Observable.just(library)
            .subscribeOn(Schedulers.computation())
            .filter(lib -> apk.containsPackage(lib.packageName()))
        ).toList();
  }
}
