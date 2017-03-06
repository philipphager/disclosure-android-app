package de.philipphager.disclosure.feature.app.detail;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.AnalyticsProgress;

public interface AnalysisProgressView {
  void showAnalysisProgress();

  void setAnalysisProgress(AnalyticsProgress.State state);

  void setAnalysisCompleted();

  void hideAnalysisProgress();

  void resetProgress();

  void showCancel();

  void showPendingApp(App pending);
}
