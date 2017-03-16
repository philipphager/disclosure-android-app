package de.philipphager.disclosure.database.app;

import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppModel;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.database.app.model.AppWithPermissions;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermission;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository {
  private static final int SQL_ERROR = -1;
  private final AppModel.InsertApp insertApp;
  private final AppModel.UpdateApp updateApp;

  @Inject public AppRepository(App.InsertApp insertApp, App.UpdateApp updateApp) {
    this.insertApp = insertApp;
    this.updateApp = updateApp;
  }

  public long insert(BriteDatabase db, App app) {
    synchronized (this) {
      insertApp.bind(app.label(), app.packageName(), app.process(), app.sourceDir(), app.flags(),
          app.analyzedAt(),
          app.isTrusted());
      return db.executeInsert(App.TABLE_NAME, insertApp.program);
    }
  }

  public int update(BriteDatabase db, App app) {
    synchronized (this) {
      updateApp.bind(app.label(), app.process(), app.sourceDir(), app.flags(), app.analyzedAt(),
          app.isTrusted(), app.packageName());
      return db.executeUpdateDelete(App.TABLE_NAME, updateApp.program);
    }
  }

  public long insertOrUpdate(BriteDatabase db, App app) {
    synchronized (this) {
      int updatedRows = update(db, app);

      if (updatedRows == 0) {
        return insert(db, app);
      }
      return getAppId(db, app.packageName());
    }
  }

  public int delete(BriteDatabase db, String where) {
    synchronized (this) {
      return db.delete(App.TABLE_NAME, where);
    }
  }

  public Observable<List<App>> all(BriteDatabase db) {
    CursorToListMapper<App> cursorToList = new CursorToListMapper<>(App.FACTORY.selectAllMapper());

    return db.createQuery(App.TABLE_NAME, App.SELECTALL)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<App> byPackageName(BriteDatabase db, String packageName) {
    SqlDelightStatement selectByPackage = App.FACTORY.selectByPackage(packageName);
    CursorToListMapper<App> cursorToList =
        new CursorToListMapper<>(App.FACTORY.selectByLibraryMapper());

    return db.createQuery(selectByPackage.tables, selectByPackage.statement, selectByPackage.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList)
        .flatMap(apps -> Observable.from(apps).first());
  }

  public Observable<List<App>> byLibrary(BriteDatabase db, String libraryId) {
    SqlDelightStatement selectByLibrary = App.FACTORY.selectByLibrary(libraryId);
    CursorToListMapper<App> cursorToList =
        new CursorToListMapper<>(App.FACTORY.selectByLibraryMapper());

    return db.createQuery(selectByLibrary.tables, selectByLibrary.statement, selectByLibrary.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<App>> byFeature(BriteDatabase db, String featureId) {
    SqlDelightStatement selectByFeature = App.FACTORY.selectByFeature(featureId);
    CursorToListMapper<App> cursorToList =
        new CursorToListMapper<>(App.FACTORY.selectByFeatureMapper());

    return db.createQuery(selectByFeature.tables, selectByFeature.statement, selectByFeature.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<App.Info>> allInfos(BriteDatabase db) {
    CursorToListMapper<App.Info> cursorToList = new CursorToListMapper<>(App.Info.MAPPER);

    return db.createQuery(App.TABLE_NAME, App.SELECTALLINFOS)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<AppWithLibraries>> allWithLibraryCount(BriteDatabase db) {
    CursorToListMapper<AppWithLibraries> cursorToList =
        new CursorToListMapper<>(AppWithLibraries.MAPPER);

    return db.createQuery(Arrays.asList(App.TABLE_NAME, LibraryApp.TABLE_NAME),
        App.SELECTALLWITHLIBRARYCOUNT)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<AppWithPermissions>> byLibraryWithPermissionCount(BriteDatabase db,
      String libraryId) {
    SqlDelightStatement selectByLibrary = App.FACTORY.selectAllWithPermissionCount(libraryId);
    CursorToListMapper<AppWithPermissions> cursorToList =
        new CursorToListMapper<>(AppWithPermissions.MAPPER);

    return db.createQuery(selectByLibrary.tables, selectByLibrary.statement, selectByLibrary.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<AppReport>> selectByQuery(BriteDatabase db, String query) {
    String likeClause = "%" + query + "%";
    SqlDelightStatement selectByQuery =
        App.FACTORY.selectByQuery(likeClause, likeClause, likeClause);

    CursorToListMapper<AppReport> cursorToList =
        new CursorToListMapper<>(AppReport.MAPPER);

    return db.createQuery(selectByQuery.tables, selectByQuery.statement, selectByQuery.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<AppWithLibraries>> selectTrusted(BriteDatabase db, boolean isTrusted) {
    SqlDelightStatement selectTrusted = App.FACTORY.selectTrusted(isTrusted);

    CursorToListMapper<AppWithLibraries> cursorToList =
        new CursorToListMapper<>(AppWithLibraries.MAPPER);

    return db.createQuery(selectTrusted.tables, selectTrusted.statement, selectTrusted.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<AppReport>> selectReport(BriteDatabase db) {
    CursorToListMapper<AppReport> cursorToList = new CursorToListMapper<>(AppReport.MAPPER);

    List<String> tables = Arrays.asList(App.TABLE_NAME, LibraryApp.TABLE_NAME, "Report",
        AppLibraryPermission.TABLE_NAME);

    return db.createQuery(tables, App.SELECTREPORT)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  private long getAppId(BriteDatabase db, String packageName) {
    SqlDelightStatement selectAppId = App.FACTORY.selectAppId(packageName);
    Cursor cursor = db.query(selectAppId.statement, selectAppId.args);

    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      return cursor.getLong(0);
    }
    return SQL_ERROR;
  }
}
