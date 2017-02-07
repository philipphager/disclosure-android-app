package de.philipphager.disclosure.feature.app.overview.search;

import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import java.util.List;
import rx.Observable;

public interface AppSearchView {
  Observable<String> getSearchQuery();

  void showApps(List<AppWithLibraries> apps);

  void showEmptySearchView();

  void hideEmptySearchView();

  void finish();
}
