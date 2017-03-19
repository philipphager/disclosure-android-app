package de.philipphager.disclosure.database.permission.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.permission.model.PermissionModel;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class PermissionRepository {
  private final PermissionModel.InsertPermission insertPermission;
  private final PermissionModel.UpdatePermission updatePermission;

  @Inject public PermissionRepository(Permission.InsertPermission insertPermission,
      Permission.UpdatePermission updatePermission) {
    this.insertPermission = insertPermission;
    this.updatePermission = updatePermission;
  }

  public long insert(BriteDatabase db, Permission permission) {
    synchronized (this) {
      insertPermission.bind(
          permission.id(),
          permission.title(),
          permission.description(),
          permission.protectionLevel(),
          permission.permissionGroup());

      return db.executeInsert(insertPermission.table, insertPermission.program);
    }
  }

  public int update(BriteDatabase db, Permission permission) {
    synchronized (this) {
      updatePermission.bind(
          permission.title(),
          permission.description(),
          permission.protectionLevel(),
          permission.permissionGroup(),
          permission.id());

      return db.executeUpdateDelete(updatePermission.table, updatePermission.program);
    }
  }

  public Observable<List<Permission>> all(BriteDatabase db) {
    CursorToListMapper<Permission> cursorToList =
        new CursorToListMapper<>(Permission.FACTORY.selectAllMapper());
    SqlDelightStatement selectAll = Permission.FACTORY.selectAll();

    return db.createQuery(selectAll.tables, selectAll.statement)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Permission>> byApp(BriteDatabase db, long appId, boolean isGranted) {
    SqlDelightStatement selectByApp = Permission.FACTORY.selectByApp(appId, isGranted);
    CursorToListMapper<Permission> cursorToList =
        new CursorToListMapper<>(Permission.FACTORY.selectByAppMapper());

    return db.createQuery(selectByApp.tables, selectByApp.statement, selectByApp.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<AppPermission>> byApp(BriteDatabase db, long appId) {
    SqlDelightStatement selectByApp = AppPermission.FACTORY.selectByApp(appId);
    CursorToListMapper<AppPermission> cursorToList =
        new CursorToListMapper<>(AppPermission.FACTORY.selectByAppMapper());

    return db.createQuery(selectByApp.tables, selectByApp.statement, selectByApp.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Permission>> byAppAndLibrary(BriteDatabase db, long appId, String libraryId) {
    SqlDelightStatement selectByAppAndLibrary = Permission.FACTORY.selectByAppAndLibrary(appId, libraryId);
    CursorToListMapper<Permission> cursorToList =
        new CursorToListMapper<>(Permission.FACTORY.selectByAppAndLibraryMapper());

    return db.createQuery(selectByAppAndLibrary.tables, selectByAppAndLibrary.statement, selectByAppAndLibrary.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }
}
