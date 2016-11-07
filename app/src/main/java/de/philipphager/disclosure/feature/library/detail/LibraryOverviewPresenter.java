package de.philipphager.disclosure.feature.library.detail;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.AppService;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class LibraryOverviewPresenter {
  private final AppService appService;
  private CompositeSubscription subscriptions;
  private LibraryOverviewView view;
  private Library library;

  @Inject public LibraryOverviewPresenter(AppService appService) {
    this.appService = appService;
  }

  public void onCreate(LibraryOverviewView view, Library library) {
    this.view = view;
    this.library = library;
    this.subscriptions = new CompositeSubscription();

    loadAppsByLibrary();
  }

  public void onDestroy() {
    this.subscriptions.unsubscribe();
  }

  private void loadAppsByLibrary() {
    subscriptions.add(appService.byLibrary(library.id())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(apps -> {
          view.showApps(apps);
        }, throwable -> {
          Timber.e(throwable, "while loading apps by library");
        }));
  }
}
