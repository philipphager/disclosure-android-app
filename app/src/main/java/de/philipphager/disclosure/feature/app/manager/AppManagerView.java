package de.philipphager.disclosure.feature.app.manager;

import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.feature.app.detail.AnalysisProgressView;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface AppManagerView extends Navigates, AnalysisProgressView {
  void showCurrentAnalysedApp(String appLabel);

  void showAppCount(int count);

  void showLibraryCount(int count);

  void showCancelPendingApps(int count);

  void show(List<AppReport> appReports);

  void startActionMode();

  void showActionModeTitle(String title);

  boolean hasActionMode();

  void notify(String message);
}
