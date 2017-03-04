package de.philipphager.disclosure.feature.app.manager.list;

import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface AppManagerView extends Navigates {
  void showAppCount(int count);

  void showLibraryCount(int count);

  void show(List<AppReport> appReports);

  void startActionMode();

  void showActionModeTitle(String title);

  boolean hasActionMode();

  void notify(String message);
}
