package de.philipphager.disclosure.feature.app.overview.trusted;

import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.feature.app.overview.list.AppListView;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.service.app.filter.SortBy;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class TrustedAppListPresenter {
  private final AppService appService;
  private final Preference<SortBy> sortByPreference;
  private CompositeSubscription subscriptions;
  private AppListView view;

  @Inject public TrustedAppListPresenter(AppService appService,
      @AppListSortBy Preference<SortBy> sortByPreference) {
    this.appService = appService;
    this.sortByPreference = sortByPreference;
  }

  public void onViewCreated(AppListView view) {
    this.view = view;
    subscriptions = new CompositeSubscription();
    fetchListSortingPreference();
    fetchUserApps();
  }

  public void onDestroyView() {
    subscriptions.unsubscribe();
  }

  private void fetchUserApps() {
    subscriptions.add(appService.allReports(sortByPreference.get())
        .flatMap(appReports -> Observable.from(appReports)
          .filter(appReport -> appReport.App().isTrusted())
          .toList())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(appReports -> {
          int libraryCount = 0;

          for (AppReport appReport : appReports) {
            libraryCount += appReport.libraryCount();
          }

          view.showAppCount(appReports.size());
          view.showLibraryCount(libraryCount);
          view.show(appReports);
        }, throwable -> {
          Timber.e(throwable, "while loading all apps");
        }));
  }

  private void fetchListSortingPreference() {
    subscriptions.add(sortByPreference.asObservable()
        .subscribeOn(Schedulers.io())
        .subscribe(option -> {
          fetchUserApps();
        }));
  }

  public void onAppClicked(AppReport appReport) {
    view.navigate().toAppDetail(appReport.App());
  }
}
