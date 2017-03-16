package de.philipphager.disclosure.database.permission.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class AppLibraryPermission implements AppLibraryPermissionModel {
  public static final AppLibraryPermissionModel.Factory<AppLibraryPermission>
      FACTORY = new AppLibraryPermissionModel.Factory<>(AppLibraryPermission::create);

  public static AppLibraryPermission create(Long appId, String libraryId, String permissionId) {
    return new AutoValue_AppLibraryPermission(appId, libraryId, permissionId);
  }

  public abstract Long appId();

  public abstract String libraryId();

  public abstract String permissionId();
}
