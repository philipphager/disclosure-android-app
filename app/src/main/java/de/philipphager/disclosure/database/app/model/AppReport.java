package de.philipphager.disclosure.database.app.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class AppReport implements AppModel.ReportModel {
  public static final AppModel.ReportMapper MAPPER = new AppModel.ReportMapper((App, libraryCount, permissionCount) -> create(App, libraryCount, permissionCount), App.FACTORY);

  public static AppReport create(AppModel app, long libraryCount, long permissionCount) {
    //TODO: Fix whe
    return new AutoValue_AppReport((App) app, libraryCount, permissionCount);
  }

  /**
   * SqlDelight 0.6.0 alias bug in projection.
   * Tracked in Github issue 592.
   * Rename to lower case method name if fixed!
   * https://github.com/square/sqldelight/issues/592
   */
  @SuppressWarnings("PMD.MethodNamingConventions")
  public abstract App App();

  public abstract long libraryCount();

  public abstract long permissionCount();

  public boolean librariesDetected() {
    return libraryCount() > 0;
  }

  public boolean wasAnalyzed() {
    return App().analyzedAt() != null;
  }
}
