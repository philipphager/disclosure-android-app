package de.philipphager.disclosure.database.app.model;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import static de.philipphager.disclosure.database.app.model.App.FACTORY;

@AutoValue public abstract class AppInfo implements AppModel.SelectAllInfosModel {
  public static final RowMapper<AppInfo> MAPPER =
      FACTORY.selectAllInfosMapper(AppInfo::create);

  public static AppInfo create(String packageName, int versionCode) {
    return new AutoValue_AppInfo(packageName, versionCode);
  }

  public abstract String packageName();

  public abstract int versionCode();
}