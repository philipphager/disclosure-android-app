package de.philipphager.disclosure.database.permission;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermission;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import javax.inject.Singleton;

@Module public class PermissionModule {
  @Provides @Singleton
  public Permission.InsertPermission getInsertPermissionStatement(DatabaseManager databaseManager) {
    return new Permission.InsertPermission(databaseManager.getSQLite());
  }

  @Provides @Singleton
  public Permission.UpdatePermission getUpdatePermissionStatement(DatabaseManager databaseManager) {
    return new Permission.UpdatePermission(databaseManager.getSQLite());
  }

  @Provides @Singleton
  public AppPermission.InsertPermissionApp getInsertPermissionAppStatement(
      DatabaseManager databaseManager) {
    return new AppPermission.InsertPermissionApp(databaseManager.getSQLite());
  }

  @Provides @Singleton
  public AppLibraryPermission.InsertAppLibraryPermission getInsertAllLibraryPermissionStatement(
      DatabaseManager databaseManager) {
    return new AppLibraryPermission.InsertAppLibraryPermission(databaseManager.getSQLite());
  }
}
