package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermission;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.permission.model.PermissionGroup;
import de.philipphager.disclosure.database.permission.repositories.AppLibraryPermissionRepository;
import de.philipphager.disclosure.database.permission.repositories.AppPermissionRepository;
import de.philipphager.disclosure.database.permission.repositories.PermissionGroupRepository;
import de.philipphager.disclosure.database.permission.repositories.PermissionRepository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class PermissionService {
  private final DatabaseManager databaseManager;
  private final PermissionRepository permissionRepository;
  private final PermissionGroupRepository permissionGroupRepository;
  private final AppPermissionRepository appPermissionRepository;
  private final AppLibraryPermissionRepository appLibraryPermissionRepository;

  @Inject public PermissionService(DatabaseManager databaseManager,
      PermissionRepository permissionRepository,
      PermissionGroupRepository permissionGroupRepository,
      AppPermissionRepository appPermissionRepository,
      AppLibraryPermissionRepository appLibraryPermissionRepository) {
    this.databaseManager = databaseManager;
    this.permissionRepository = permissionRepository;
    this.permissionGroupRepository = permissionGroupRepository;
    this.appPermissionRepository = appPermissionRepository;
    this.appLibraryPermissionRepository = appLibraryPermissionRepository;
  }

  public void insertOrUpdate(List<Permission> permissions) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Permission permission : permissions) {
        PermissionGroup group = getGroup(permission.permissionGroup());

        if (group != null) {
          insertOrUpdate(group);
        }

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

  public void insertForAppAndLibrary(App app, Library library, List<Permission> permissions) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (Permission permission : permissions) {
        AppLibraryPermission appLibraryPermission =
            AppLibraryPermission.create(app.id(), library.id(), permission.id());
        appLibraryPermissionRepository.insert(db, appLibraryPermission);
      }

      transaction.markSuccessful();
    }
  }

  public void insertOrUpdate(PermissionGroup permissionGroup) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      int updatedRows = permissionGroupRepository.update(db, permissionGroup);

      if (updatedRows == 0) {
        permissionGroupRepository.insert(db, permissionGroup);
      }

      transaction.markSuccessful();
    }
  }

  public Observable<List<Permission>> all() {
    BriteDatabase db = databaseManager.get();
    return permissionRepository.all(db);
  }

  public Observable<List<Permission>> byApp(App app, boolean isGranted) {
    BriteDatabase db = databaseManager.get();
    return permissionRepository.byApp(db, app.id(), isGranted);
  }

  public Observable<List<Permission>> byAppAndLibrary(App app, Library library) {
    BriteDatabase db = databaseManager.get();
    return permissionRepository.byAppAndLibrary(db, app.id(), library.id());
  }

  private PermissionGroup getGroup(String groupId) {
    return groupId != null ? PermissionGroup.create(groupId, "", "") : null;
  }
}
