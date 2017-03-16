package de.philipphager.disclosure.feature.analyser;

import com.google.auto.value.AutoValue;
import de.philipphager.disclosure.database.app.model.App;

@AutoValue public abstract class AnalyticsProgress {
  public static AnalyticsProgress create(App app, State state) {
    return new AutoValue_AnalyticsProgress(app, state);
  }

  public abstract App app();

  public abstract State state();

  public enum State {
    START, EXTRACT_APK, FILTER_METHODS, ANALYSE_METHODS, COMPLETE, CANCEL
  }
}
