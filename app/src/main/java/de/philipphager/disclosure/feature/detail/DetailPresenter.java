package de.philipphager.disclosure.feature.detail;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedLibraries;
import de.philipphager.disclosure.service.LibraryService;
import javax.inject.Inject;
import timber.log.Timber;

public class DetailPresenter {
  private final AnalyseUsedLibraries analyseUsedLibraries;
  private final LibraryService libraryService;
  private DetailView view;
  private App app;

  @Inject public DetailPresenter(AnalyseUsedLibraries analyseUsedLibraries,
      LibraryService libraryService) {
    this.analyseUsedLibraries = analyseUsedLibraries;
    this.libraryService = libraryService;
  }

  public void onCreate(DetailView view, App app) {
    this.view = view;
    this.app = app;

    intiView();

    libraryService.byApp(app)
        .subscribe(libraries -> {
          Timber.d("loaded %s libraries", libraries);
          view.notify(String.format("loaded %s libraries", libraries));
        }, Timber::e);
  }

  private void intiView() {
    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());
  }

  public void onAnalyseButtonClick() {
    analyseUsedLibraries.analyse(app)
        .subscribe(libraries -> {
          Timber.d("found %s libraries", libraries);
        }, Timber::e);
  }
}
