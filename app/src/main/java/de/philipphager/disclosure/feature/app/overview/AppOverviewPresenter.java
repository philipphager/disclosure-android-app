package de.philipphager.disclosure.feature.app.overview;

import android.view.MenuItem;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.analyser.app.usecase.AnalyseApps;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.service.app.filter.SortBy;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AppOverviewPresenter {
  private final AppService appService;
  private final AnalyseApps analyseApps;
  private final Preference<SortBy> sortBy;
  private CompositeSubscription subscriptions;
  private AppOverviewView view;

  @Inject public AppOverviewPresenter(AppService appService,
      AnalyseApps analyseApps,
      @AppListSortBy Preference<SortBy> sortBy) {
    this.appService = appService;
    this.analyseApps = analyseApps;
    this.sortBy = sortBy;
  }

  public void onViewCreated(AppOverviewView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();
    analyseAppLibraries();
  }

  public void onDestroyView() {
    subscriptions.unsubscribe();
  }

  private void analyseAppLibraries() {
    subscriptions.add(appService.all()
            .first()
            .flatMap(analyseApps::analyse)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(libraries -> {
              Timber.d("App analysis finished");
            }, throwable -> {
              Timber.e(throwable, "while analysing all apps");
            }));
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_sort_alphabetical:
        sortBy.set(SortBy.NAME);
        return true;
      case R.id.action_sort_library_count:
        sortBy.set(SortBy.LIBRARY_COUNT);
        return true;
      case R.id.action_search:
        view.navigate().toSearch();
        return true;
      default:
        return false;
    }
  }
}
