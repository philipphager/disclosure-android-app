package de.philipphager.disclosure.feature.app.overview.list;

import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface AppListView extends Navigates {
  void showAppCount(int count);

  void showLibraryCount(int count);

  void show(List<AppWithLibraries> apps);
}
