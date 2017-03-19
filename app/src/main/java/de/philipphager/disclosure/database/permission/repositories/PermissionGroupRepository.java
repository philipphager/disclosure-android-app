package de.philipphager.disclosure.database.permission.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.permission.model.PermissionGroup;
import de.philipphager.disclosure.database.permission.model.PermissionGroupModel;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class PermissionGroupRepository {
  private final PermissionGroupModel.InsertPermissionGroup insertGroup;
  private final PermissionGroupModel.UpdatePermissionGroup updateGroup;

  @Inject public PermissionGroupRepository(PermissionGroupModel.InsertPermissionGroup insertGroup,
      PermissionGroupModel.UpdatePermissionGroup updateGroup) {
    this.insertGroup = insertGroup;
    this.updateGroup = updateGroup;
  }

  public long insert(BriteDatabase db, PermissionGroup permissionGroup) {
    synchronized (this) {
      insertGroup.bind(permissionGroup.id(),
          permissionGroup.title(),
          permissionGroup.description());

      return db.executeInsert(insertGroup.table, insertGroup.program);
    }
  }

  public int update(BriteDatabase db, PermissionGroup permissionGroup) {
    synchronized (this) {
      updateGroup.bind(permissionGroup.title(),
          permissionGroup.description(),
          permissionGroup.id());

      return db.executeUpdateDelete(updateGroup.table, updateGroup.program);
    }
  }

  public Observable<List<PermissionGroup>> all(BriteDatabase db) {
    CursorToListMapper<PermissionGroup> cursorToList =
        new CursorToListMapper<>(PermissionGroup.FACTORY.selectAllMapper());
    SqlDelightStatement selectAll = PermissionGroup.FACTORY.selectAll();

    return db.createQuery(selectAll.tables, selectAll.statement)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<PermissionGroup>> byId(BriteDatabase db, String id) {
    SqlDelightStatement selectById = PermissionGroup.FACTORY.selectById(id);
    CursorToListMapper<PermissionGroup> cursorToList =
        new CursorToListMapper<>(PermissionGroup.FACTORY.selectByIdMapper());

    return db.createQuery(selectById.tables, selectById.statement, selectById.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }
}
