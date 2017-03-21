package de.philipphager.disclosure.database.method.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.method.model.Method;
import de.philipphager.disclosure.database.method.model.ProtectedMethod;
import de.philipphager.disclosure.database.method.model.ProtectedMethodModel;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

import static de.philipphager.disclosure.database.method.model.Method.MAPPER;

public class MethodRepository {
  private final ProtectedMethod.InsertMethod insertMethod;

  @Inject public MethodRepository(ProtectedMethodModel.InsertMethod insertMethod) {
    this.insertMethod = insertMethod;
  }

  public long insert(BriteDatabase db, ProtectedMethod protectedMethod) {
    synchronized (this) {
      insertMethod.bind(
          protectedMethod.permissionId(),
          protectedMethod.declaringType(),
          protectedMethod.returnType(),
          protectedMethod.name(),
          protectedMethod.argTypes());

      return db.executeInsert(insertMethod.table, insertMethod.program);
    }
  }

  public Observable<List<ProtectedMethod>> all(BriteDatabase db) {
    CursorToListMapper<ProtectedMethod> cursorToList =
        new CursorToListMapper<>(ProtectedMethod.FACTORY.selectAllMapper());
    SqlDelightStatement selectAll = ProtectedMethod.FACTORY.selectAll();

    return db.createQuery(selectAll.tables, selectAll.statement)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Method>> byPermission(BriteDatabase db, String permissionId) {
    SqlDelightStatement selectByPermission =
        ProtectedMethod.FACTORY.selectByPermission(permissionId);
    CursorToListMapper<Method> cursorToList = new CursorToListMapper<Method>(MAPPER);

    return db.createQuery(selectByPermission.tables, selectByPermission.statement,
        selectByPermission.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }
}
