package de.philipphager.disclosure.database.permission.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.AppPermissionModel;
import javax.inject.Inject;

public class AppPermissionRepository {
  private final AppPermissionModel.InsertPermissionApp insertPermissionApp;

  @Inject public AppPermissionRepository(AppPermission.InsertPermissionApp insertPermissionApp) {
    this.insertPermissionApp = insertPermissionApp;
  }

  public long insert(BriteDatabase db, AppPermission appPermission) {
    synchronized (this) {
      insertPermissionApp.bind(appPermission.appId(),
          appPermission.permissionId(),
          appPermission.isGranted());

      return db.executeInsert(insertPermissionApp.table, insertPermissionApp.program);
    }
  }
}
