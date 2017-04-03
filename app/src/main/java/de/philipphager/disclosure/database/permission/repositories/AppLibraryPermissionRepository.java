package de.philipphager.disclosure.database.permission.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermission;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermissionModel;
import javax.inject.Inject;

public class AppLibraryPermissionRepository {
  private final AppLibraryPermissionModel.InsertAppLibraryPermission insertAppLibraryPermission;

  @Inject public AppLibraryPermissionRepository(
      AppLibraryPermission.InsertAppLibraryPermission insertAppLibraryPermission) {
    this.insertAppLibraryPermission = insertAppLibraryPermission;
  }

  public long insert(BriteDatabase db, AppLibraryPermission appLibraryPermission) {
    synchronized (this) {
      insertAppLibraryPermission.bind(
          appLibraryPermission.appId(),
          appLibraryPermission.libraryId(),
          appLibraryPermission.permissionId());

      return db.executeInsert(insertAppLibraryPermission.table, insertAppLibraryPermission.program);
    }
  }
}
