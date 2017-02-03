package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermission;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.permission.repositories.AppLibraryPermissionRepository;
import de.philipphager.disclosure.database.permission.repositories.AppPermissionRepository;
import de.philipphager.disclosure.database.permission.repositories.PermissionRepository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class PermissionService {
  private final DatabaseManager databaseManager;
  private final PermissionRepository permissionRepository;
  private final AppPermissionRepository appPermissionRepository;
  private final AppLibraryPermissionRepository appLibraryPermissionRepository;

  @Inject public PermissionService(DatabaseManager databaseManager,
      PermissionRepository permissionRepository,
      AppPermissionRepository appPermissionRepository,
      AppLibraryPermissionRepository appLibraryPermissionRepository) {
    this.databaseManager = databaseManager;
    this.permissionRepository = permissionRepository;
    this.appPermissionRepository = appPermissionRepository;
    this.appLibraryPermissionRepository = appLibraryPermissionRepository;
  }

  public void insertOrUpdate(List<Permission> permissions) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Permission permission : permissions) {
        int updatedRows = permissionRepository.update(db, permission);

        if (updatedRows == 0) {
          permissionRepository.insert(db, permission);
        }
      }
      transaction.markSuccessful();
    }
  }

  public void insertForApp(List<AppPermission> appPermissions) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (AppPermission appPermission : appPermissions) {
        appPermissionRepository.insert(db, appPermission);
      }

      transaction.markSuccessful();
    }
  }

  public void insertForAppAndLibrary(List<AppLibraryPermission> appLibraryPermissions) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (AppLibraryPermission appLibraryPermission : appLibraryPermissions) {
        appLibraryPermissionRepository.insert(db, appLibraryPermission);
      }

      transaction.markSuccessful();
    }
  }

  public Observable<List<Permission>> all() {
    BriteDatabase db = databaseManager.get();
    return permissionRepository.all(db);
  }

  public Observable<List<Permission>> byApp(App app) {
    BriteDatabase db = databaseManager.get();
    return permissionRepository.byApp(db, app.id());
  }

  public Observable<List<Permission>> byAppAndLibrary(App app, Library library) {
    BriteDatabase db = databaseManager.get();
    return permissionRepository.byAppAndLibrary(db, app.id(), library.id());
  }
}
