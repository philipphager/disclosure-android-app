package de.philipphager.disclosure.database.permission.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class AppPermission implements AppPermissionModel {
  public static final Factory<AppPermission> FACTORY = new Factory<>(AppPermission::create);

  public static AppPermission create(Long appId, String permissionId, Boolean isGranted) {
    return new AutoValue_AppPermission(appId, permissionId, isGranted);
  }

  public abstract Long appId();

  public abstract String permissionId();

  public abstract Boolean isGranted();
}
