package de.philipphager.disclosure.feature.library.detail;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.FeatureService;
import de.philipphager.disclosure.service.app.AppService;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class LibraryOverviewPresenter {
  private final AppService appService;
  private final FeatureService featureService;
  private CompositeSubscription subscriptions;
  private LibraryOverviewView view;
  private Library library;

  @Inject public LibraryOverviewPresenter(AppService appService,
      FeatureService featureService) {
    this.appService = appService;
    this.featureService = featureService;
  }

  public void onCreate(LibraryOverviewView view, Library library) {
    this.view = view;
    this.library = library;
    this.subscriptions = new CompositeSubscription();

    loadFeatures();
    loadApps();
  }

  public void onDestroy() {
    this.subscriptions.unsubscribe();
  }

  private void loadApps() {
    subscriptions.add(appService.byLibrary(library.id())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(apps -> {
          view.showApps(apps);
        }, throwable -> {
          Timber.e(throwable, "while loading apps by library");
        }));
  }

  private void loadFeatures() {
    subscriptions.add(featureService.byLibrary(library.id())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(features -> {
          Timber.d("library %s has following features %s", library, features);
        }, throwable -> {
          Timber.e(throwable, "while loading apps by library");
        }));
  }
}
