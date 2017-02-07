package de.philipphager.disclosure.feature.app.overview;

import android.view.MenuItem;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.service.app.filter.SortBy;
import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;

public class AppOverviewPresenter {
  private final Preference<SortBy> sortBy;
  private CompositeSubscription subscriptions;
  private AppOverviewView view;

  @Inject public AppOverviewPresenter(@AppListSortBy Preference<SortBy> sortBy) {
    this.sortBy = sortBy;
  }

  public void onCreate(AppOverviewView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();
  }

  public void onDestroy() {
    subscriptions.unsubscribe();
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
