package de.philipphager.disclosure.feature.app.manager;

import android.view.MenuItem;
import android.widget.Checkable;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.feature.analyser.app.usecase.AnalyseAppLibraryPermissions;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.service.app.filter.SortBy;
import de.philipphager.disclosure.util.ui.StringProvider;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class AppManagerPresenter {
  private final AppService appService;
  private final Preference<SortBy> sortBy;
  private final AnalyseAppLibraryPermissions analyseAppLibraryPermissions;
  private final StringProvider stringProvider;
  private CompositeSubscription subscriptions;
  private AppManagerView view;
  private Set<AppReport> selectedApps;

  @Inject public AppManagerPresenter(AppService appService,
      @AppListSortBy Preference<SortBy> sortBy,
      AnalyseAppLibraryPermissions analyseAppLibraryPermissions,
      StringProvider stringProvider) {
    this.appService = appService;
    this.sortBy = sortBy;
    this.analyseAppLibraryPermissions = analyseAppLibraryPermissions;
    this.stringProvider = stringProvider;
  }

  public void onViewCreated(AppManagerView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();
    fetchListSortingPreference();
    fetchUserApps();
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
        }));
  }

  public void onAppClicked(Checkable checkable, AppReport appReport) {
    if (!view.hasActionMode()) {
      navigateToAppDetail(appReport);
    } else {
      if (selectedApps.contains(appReport)) {
        deselectApp(checkable, appReport);
      } else {
        selectApp(checkable, appReport);
      }
    }
  }

  public void onAppLongClicked(Checkable checkable, AppReport appReport) {
    if (!view.hasActionMode()) {
      view.startActionMode();
      selectedApps = new HashSet<>();
      selectApp(checkable, appReport);
    }
  }

  public void onAnalyzeAppsClicked() {
    ensureNotNull(selectedApps, "must start action mode before calling action mode menu action");
    subscriptions.add(Observable.from(selectedApps)
        .map(AppReport::App)
        .flatMap(analyseAppLibraryPermissions::run)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(permissions -> {

        }, throwable -> {
          Timber.e(throwable, "while bulk analyzing apps");
        }));
  }

  private void navigateToAppDetail(AppReport appReport) {
    view.navigate().toAppDetail(appReport.App());
  }

  private void selectApp(Checkable checkable, AppReport appReport) {
    if (appReport.librariesDetected()) {
      ensureNotNull(selectedApps, "must start action mode before selecting app");
      checkable.setChecked(true);
      selectedApps.add(appReport);
      updateSelectedAppsTitle();
    } else {
      view.notify("Nothing to analyze in this app");
    }
  }

  private void updateSelectedAppsTitle() {
    ensureNotNull(selectedApps, "must start action mode before updating title");
    String title = stringProvider.getPlural(R.plurals.fragment_app_list_app_analyze_apps,
        selectedApps.size(),
        selectedApps.size());
    view.showActionModeTitle(title);
  }

  private void deselectApp(Checkable checkable, AppReport appReport) {
    selectedApps.remove(appReport);
    checkable.setChecked(false);
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
}
