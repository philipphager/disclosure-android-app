package de.philipphager.disclosure.feature.app.manager;

import android.view.MenuItem;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.feature.analyser.AppAnalyticsService;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.service.AppService;
import de.philipphager.disclosure.service.sort.SortBy;
import de.philipphager.disclosure.util.ui.StringProvider;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AppManagerPresenter {
  private final AppService appService;
  private final Preference<SortBy> sortBy;
  private final AppAnalyticsService appAnalyticsService;
  private final StringProvider stringProvider;
  private CompositeSubscription subscriptions;
  private AppManagerView view;

  @Inject public AppManagerPresenter(AppService appService,
      @AppListSortBy Preference<SortBy> sortBy,
      AppAnalyticsService appAnalyticsService,
      StringProvider stringProvider) {
    this.appService = appService;
    this.sortBy = sortBy;
    this.appAnalyticsService = appAnalyticsService;
    this.stringProvider = stringProvider;
  }

  public void onViewCreated(AppManagerView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();
    fetchListSortingPreference();
    fetchUserApps();
    fetchAnalysisUpdates();
  }

  public void onDestroyView() {
    this.subscriptions.clear();
    this.view = null;
  }

  private void fetchUserApps() {
    subscriptions.add(appService.allReports(sortBy.get())
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
    subscriptions.add(sortBy.asObservable()
        .subscribeOn(Schedulers.io())
        .subscribe(option -> {
          fetchUserApps();
        }, throwable -> Timber.d(throwable, "while fetching apps")));
  }

  public void onAppClicked(AppReport appReport) {
    if (!view.hasActionMode()) {
      navigateToAppDetail(appReport);
    } else {
      toggleSelectApp(appReport);
    }
  }

  public void onAppLongClicked(AppReport appReport) {
    if (!view.hasActionMode()) {
      view.startActionMode();
      toggleSelectApp(appReport);
    }
  }

  public void onAnalyzeSelectedAppsClicked() {
    subscriptions.add(Observable.from(view.getSelectedApps())
        .subscribeOn(Schedulers.io())
        .doOnNext(appReport -> appAnalyticsService.enqueue(appReport.App()))
        .subscribe(appReport -> {}, throwable -> {
          Timber.e(throwable, "while adding apps to analytics queue");
        }));
  }

  private void fetchAnalysisUpdates() {
    subscriptions.add(appAnalyticsService.getProgress()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(progress -> {
          view.showCurrentAnalysedApp(progress.app().label());
          view.setAnalysisProgress(progress.state());
        }, Timber::e));

    subscriptions.add(appAnalyticsService.getPendingApps()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(pendingApps -> {
          view.showCancelPendingApps(pendingApps.size());
        }, Timber::e));
  }

  public void onEndActionMode() {
    view.clearSelection();
  }

  private void navigateToAppDetail(AppReport appReport) {
    view.navigate().toAppDetail(appReport.App());
  }

  private void updateSelectedAppsTitle() {
    List<AppReport> selectedApps = view.getSelectedApps();

    String title = stringProvider.getPlural(R.plurals.fragment_app_manager_analyse_selected_apps,
        selectedApps.size(),
        selectedApps.size());
    view.showActionModeTitle(title);
  }

  private void toggleSelectApp(AppReport appReport) {
    view.toggleSelect(appReport);
    updateSelectedAppsTitle();
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_sort_alphabetical:
        sortBy.set(SortBy.NAME);
        return true;
      case R.id.action_sort_library_count:
        sortBy.set(SortBy.LIBRARY_COUNT);
        return true;
      case R.id.action_sort_permission_count:
        sortBy.set(SortBy.PERMISSION_COUNT);
        return true;
      case R.id.action_sort_analyzed_at:
        sortBy.set(SortBy.ANALYZED_AT);
        return true;
      case R.id.action_search:
        view.navigate().toSearch();
        return true;
      default:
        return false;
    }
  }

  public void onCancelPendingAppsClicked() {
    appAnalyticsService.removePendingApps();
  }
}
