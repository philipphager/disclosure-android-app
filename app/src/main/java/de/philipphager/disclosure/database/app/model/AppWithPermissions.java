package de.philipphager.disclosure.database.app.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class AppWithPermissions
    implements App.SelectAllWithPermissionCountModel {
  public static final AppModel.SelectAllWithPermissionCountMapper MAPPER =
      new AppModel.SelectAllWithPermissionCountMapper(
          (app, permissionCount) -> create((App) app, permissionCount), App.FACTORY);

  public static AppWithPermissions create(App app, long permissionCount) {
    return new AutoValue_AppWithPermissions(app, permissionCount);
  }

  /**
   * SqlDelight 0.6.0 alias bug in projection.
   * Tracked in Github issue 592.
   * Rename to lower case method name if fixed!
   * https://github.com/square/sqldelight/issues/592
   */
  @SuppressWarnings("PMD.MethodNamingConventions")
  public abstract App App();

  public abstract long permissionCount();

  public int permissionCountInt() {
    return (int) permissionCount();
  }
}
