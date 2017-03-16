package de.philipphager.disclosure.database.library.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.util.time.Date;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class LibraryRepository {
  private final Library.InsertLibrary insertLibrary;
  private final Library.UpdateLibrary updateLibrary;

  @Inject public LibraryRepository(Library.InsertLibrary insertLibrary,
      Library.UpdateLibrary updateLibrary) {
    this.insertLibrary = insertLibrary;
    this.updateLibrary = updateLibrary;
  }

  public long insert(BriteDatabase db, Library library) {
    synchronized (this) {
      insertLibrary.bind(
          library.id(),
          library.packageName(),
          library.sourceDir(),
          library.title(),
          library.subtitle(),
          library.description(),
          library.websiteUrl(),
          library.type(),
          library.createdAt(),
          library.updatedAt());

      return db.executeInsert(insertLibrary.table, insertLibrary.program);
    }
  }

  public int update(BriteDatabase db, Library library) {
    synchronized (this) {
      updateLibrary.bind(
          library.packageName(),
          library.sourceDir(),
          library.title(),
          library.subtitle(),
          library.description(),
          library.websiteUrl(),
          library.type(),
          library.createdAt(),
          library.updatedAt(),
          library.id());

      return db.executeUpdateDelete(updateLibrary.table, updateLibrary.program);
    }
  }

  public Observable<List<Library>> all(BriteDatabase db) {
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectAllMapper());

    return db.createQuery(App.TABLE_NAME, Library.SELECTALL)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<Library> byId(BriteDatabase db, String id) {
    SqlDelightStatement selectById = Library.FACTORY.selectById(id);
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectByIdMapper());

    return db.createQuery(selectById.tables, selectById.statement, selectById.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList)
        .flatMap(libraries -> Observable.from(libraries).first());
  }

  public Observable<List<Library>> byApp(BriteDatabase db, long appId) {
    SqlDelightStatement selectByApp = Library.FACTORY.selectByApp(appId);
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectByAppMapper());

    return db.createQuery(selectByApp.tables, selectByApp.statement, selectByApp.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Library>> byAppWithPermissionUpdate(BriteDatabase db, long appId) {
    SqlDelightStatement selectByApp = Library.FACTORY.selectByAppUpdateOnNewPermission(appId);
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectByAppMapper());

    return db.createQuery(selectByApp.tables, selectByApp.statement, selectByApp.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Library>> byFeature(BriteDatabase db, String featureId) {
    SqlDelightStatement selectByFeature = Library.FACTORY.selectByFeature(featureId);
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectByFeatureMapper());

    return db.createQuery(selectByFeature.tables, selectByFeature.statement, selectByFeature.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Library>> byType(BriteDatabase db, Library.Type type) {
    SqlDelightStatement selectByType = Library.FACTORY.selectByType(type);
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectByTypeMapper());

    return db.createQuery(selectByType.tables, selectByType.statement, selectByType.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<LibraryInfo>> infoByType(BriteDatabase db, Library.Type type) {
    SqlDelightStatement selectByType = Library.FACTORY.selectByTypeWithAppCount(type);
    CursorToListMapper<LibraryInfo> cursorToList = new CursorToListMapper<>(LibraryInfo.MAPPER);

    return db.createQuery(selectByType.tables, selectByType.statement, selectByType.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<OffsetDateTime> lastUpdated(BriteDatabase db) {
    return db.createQuery(Library.TABLE_NAME, Library.SELECTLASTUPDATED)
        .map(SqlBrite.Query::run)
        .map(cursor -> {
          if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return Library.FACTORY.selectLastUpdatedMapper().map(cursor);
          }
          return Date.MIN;
        });
  }

  public Observable<Long> countByType(BriteDatabase db, Library.Type type) {
    SqlDelightStatement countByType = Library.FACTORY.countByType(type);

    return db.createQuery(countByType.tables, countByType.statement, countByType.args)
        .map(SqlBrite.Query::run)
        .map(cursor -> {
          if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return Library.FACTORY.countByTypeMapper().map(cursor);
          }
          return 0L;
        });
  }

  public Observable<Long> countUsedLibrariesByType(BriteDatabase db, Library.Type type) {
    SqlDelightStatement countByType = Library.FACTORY.countUsedLibrariesByType(type);

    return db.createQuery(countByType.tables, countByType.statement, countByType.args)
        .map(SqlBrite.Query::run)
        .map(cursor -> {
          if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return Library.FACTORY.countUsedLibrariesByTypeMapper().map(cursor);
          }
          return 0L;
        });
  }
}
