package de.philipphager.disclosure.feature.analyser.library.service;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedLibraries;
import de.philipphager.disclosure.service.LibraryService;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AnalyticsService {
  private final AnalyseUsedLibraries analyseUsedLibraries;
  private final LibraryService libraryService;

  @Inject public AnalyticsService(AnalyseUsedLibraries analyseUsedLibraries,
      LibraryService libraryService) {
    this.analyseUsedLibraries = analyseUsedLibraries;
    this.libraryService = libraryService;
  }

  public void analyseLibraries(final App app) {
    analyseUsedLibraries.analyse(app)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(Observable::from)
        .subscribe(library -> {
          libraryService.addForApp(app, library);
        }, throwable -> {
          Timber.e(throwable, "while analysing used libraries.");
        });
  }
}
