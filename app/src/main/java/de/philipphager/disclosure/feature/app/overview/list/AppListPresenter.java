package de.philipphager.disclosure.feature.app.overview.list;

import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.feature.analyser.app.usecase.AnalyseApps;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.service.app.filter.SortBy;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AppListPresenter {
  private final AppService appService;
  private final AnalyseApps analyseApps;
  private final Preference<SortBy> sortByPreference;
  private CompositeSubscription subscriptions;
  private AppListView view;

  @Inject public AppListPresenter(AppService appService,
      AnalyseApps analyseApps,
      @AppListSortBy Preference<SortBy> sortByPreference) {
    this.appService = appService;
    this.analyseApps = analyseApps;
    this.sortByPreference = sortByPreference;
  }

  public void onCreate(AppListView view) {
    this.view = view;
    subscriptions = new CompositeSubscription();
    fetchListSortingPreference();
    fetchUserApps();
    analyseAppLibraries();
  }

  public void onDestroy() {
    subscriptions.unsubscribe();
  }

  private void fetchUserApps() {
    subscriptions.add(appService.allWithLibraries(sortByPreference.get())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(apps -> {
          int libraryCount = 0;

          for (AppWithLibraries app : apps) {
            libraryCount += app.libraryCountInt();
          }

          view.showAppCount(apps.size());
          view.showLibraryCount(libraryCount);
          view.show(apps);
        }, throwable -> {
          Timber.e(throwable, "while loading all apps");
        }));
  }

  private void fetchListSortingPreference() {
    sortByPreference.asObservable()
        .subscribeOn(Schedulers.io())
        .subscribe(option -> {
          fetchUserApps();
        });
  }

  private void analyseAppLibraries() {
    view.showProgress();

    subscriptions.add(
        appService.all()
            .first()
            .flatMap(analyseApps::analyse)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(() -> view.hideProgress())
            .subscribe(libraries -> {
            }, throwable -> Timber.e(throwable, "while analysing all apps")));
  }

  public void onAppClicked(AppWithLibraries appWithLibraries) {
    App app = App.builder()
        .id(appWithLibraries.id())
        .packageName(appWithLibraries.packageName())
        .label(appWithLibraries.label())
        .process(appWithLibraries.process())
        .sourceDir(appWithLibraries.sourceDir())
        .flags(appWithLibraries.flags())
        .build();

    view.navigate().toAppDetail(app);
  }
}