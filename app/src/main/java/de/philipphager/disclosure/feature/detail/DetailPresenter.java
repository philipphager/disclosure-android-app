package de.philipphager.disclosure.feature.detail;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedLibraries;
import de.philipphager.disclosure.service.LibraryService;
import de.philipphager.disclosure.util.ui.components.ScoreView;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class DetailPresenter {
  private final AnalyseUsedLibraries analyseUsedLibraries;
  private final LibraryService libraryService;
  private CompositeSubscription subscriptions;
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
    this.subscriptions = new CompositeSubscription();

    intiView();

    subscriptions.add(libraryService.byApp(app)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraries -> {
          Timber.d("loaded %s libraries", libraries);
            view.setLibraries(libraries);

          if (libraries.size() > 0) {
            view.setScore(ScoreView.Score.HIGH);
          } else {
            view.setScore(ScoreView.Score.LOW);
          }
        }, Timber::e));
  }

  public void onDestroy() {
    subscriptions.unsubscribe();
  }

  private void intiView() {
    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());
  }

  public void onAnalyseButtonClick() {
    subscriptions.add(analyseUsedLibraries.analyse(app)
        .subscribe(libraries -> {
          Timber.d("found %s libraries", libraries);
        }, Timber::e));
  }

  public void onLibraryClicked(Library library) {
    
  }
}
