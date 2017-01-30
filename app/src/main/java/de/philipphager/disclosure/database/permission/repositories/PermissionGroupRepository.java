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
  private final PermissionGroupModel.InsertPermissionGroup insert;
  private final PermissionGroupModel.UpdatePermissionGroup update;

  @Inject public PermissionGroupRepository(PermissionGroupModel.InsertPermissionGroup insert,
      PermissionGroupModel.UpdatePermissionGroup update) {
    this.insert = insert;
    this.update = update;
  }

  public long insert(BriteDatabase db, PermissionGroup permissionGroup) {
    synchronized (this) {
      insert.bind(permissionGroup.id(),
          permissionGroup.title(),
          permissionGroup.description());

      return db.executeInsert(insert.table, insert.program);
    }
  }

  public int update(BriteDatabase db, PermissionGroup permissionGroup) {
    synchronized (this) {
      update.bind(permissionGroup.title(),
          permissionGroup.description(),
          permissionGroup.id());

      return db.executeUpdateDelete(update.table, update.program);
    }
  }

  public Observable<List<PermissionGroup>> all(BriteDatabase db) {
    CursorToListMapper<PermissionGroup> cursorToList =
        new CursorToListMapper<>(PermissionGroup.FACTORY.selectAllMapper());

    return db.createQuery(PermissionGroup.TABLE_NAME, PermissionGroup.SELECTALL)
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
