package de.philipphager.disclosure.feature.app.overview.search;

import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;
import rx.Observable;

public interface AppSearchView extends Navigates {
  Observable<String> getSearchQuery();

  void showApps(List<AppReport> apps);

  void showEmptySearchView();

  void hideEmptySearchView();

  void finish();
}
