package de.philipphager.disclosure.feature.detail;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.library.service.AnalyticsService;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedLibraries;
import de.philipphager.disclosure.service.LibraryService;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPresenter {
  private final AnalyticsService analyticsService;
  private final LibraryService libraryService;
  private DetailView view;
  private App app;
  private AnalyseUsedLibraries analyseUsedLibraries;

  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public DetailPresenter(AnalyticsService analyticsService, LibraryService libraryService) {
    this.analyticsService = analyticsService;
    this.libraryService = libraryService;
  }

  public void onCreate(DetailView view, App app) {
    this.view = view;
    this.app = app;

    intiView();
  }

  private void intiView() {
    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());

    libraryService.allByApp(app)
        .subscribe(libraries -> {
          view.notify(String.format("Loaded %s", libraries));
        }, throwable -> {
          Timber.e(throwable, "while loading libraries for %s", app);
        });

    analyticsService.analyseLibraries(app);
  }
}
