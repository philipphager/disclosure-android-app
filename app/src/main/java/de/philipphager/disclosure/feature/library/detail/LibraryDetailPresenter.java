package de.philipphager.disclosure.feature.library.detail;

import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppWithPermissions;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.FeatureService;
import de.philipphager.disclosure.service.app.AppService;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class LibraryDetailPresenter {
  private final AppService appService;
  private final FeatureService featureService;
  private CompositeSubscription subscriptions;
  private LibraryDetailView view;
  private Library library;

  @Inject public LibraryDetailPresenter(AppService appService,
      FeatureService featureService) {
    this.appService = appService;
    this.featureService = featureService;
  }

  public void onCreate(LibraryDetailView view, Library library) {
    this.view = view;
    this.library = library;
    this.subscriptions = new CompositeSubscription();

    initUi();
    loadFeatures();
    loadApps();
  }

  public void onDestroy() {
    this.subscriptions.clear();
    this.view = null;
  }

  private void initUi() {
    view.setToolbarTitle(library.title());
    view.setOpenWebsiteEnabled(library.hasWebsiteUrl());
  }

  private void loadApps() {
    subscriptions.add(appService.byLibraryWithPermissions(library.id())
        .flatMap(apps -> Observable.from(apps)
            .toSortedList((appWithPermissions, appWithPermissions2) -> {
              return appWithPermissions2.permissionCountInt() - appWithPermissions.permissionCountInt();
            }))
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

  public void onAppClicked(AppWithPermissions appWithPermissions) {
    subscriptions.add(appService.byPackageName(appWithPermissions.packageName())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(app -> {
          view.navigate().toAppDetail(app);
        }, throwable -> {
          Timber.e(throwable, "while loading app");
        }));
  }

  public void onOpenWebsiteClicked() {
    ensureNotNull(library, "must have library");

    if (library.hasWebsiteUrl()) {
      view.navigate().toWebsite(library.websiteUrl());
    } else {
      view.notify(R.string.notify_library_website_unavailable);
    }
  }
}
