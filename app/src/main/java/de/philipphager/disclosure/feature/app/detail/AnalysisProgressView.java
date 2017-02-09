package de.philipphager.disclosure.feature.app.detail;

public interface AnalysisProgressView {
  void showAnalysisProgress();

  void setAnalysisProgress(State state);

  void setAnalysisCompleted();

  void hideAnalysisProgress();

  void resetProgress();

  enum State {
    DECOMPILATION, EXTRACTION, ANALYSIS
  }
}
