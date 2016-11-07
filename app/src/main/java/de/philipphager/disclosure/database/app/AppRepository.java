package de.philipphager.disclosure.database.app;

import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository {
  private static final int SQL_ERROR = -1;

  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public AppRepository() {
    // Needed for dagger injection.
  }

  public long insert(BriteDatabase db, App app) {
    synchronized (this) {
      ContentValues content = App.FACTORY.marshal(app).asContentValues();
      return db.insert(App.TABLE_NAME, content);
    }
  }

  public int update(BriteDatabase db, App app, String where) {
    synchronized (this) {
      ContentValues content = App.FACTORY.marshal(app).asContentValues();
      return db.update(App.TABLE_NAME, content, where);
    }
  }

  public long insertOrUpdate(BriteDatabase db, App app) {
    synchronized (this) {
      ContentValues content = App.FACTORY.marshal(app).asContentValues();
      long appId = db.insert(App.TABLE_NAME, content, SQLiteDatabase.CONFLICT_IGNORE);

      if (appId == SQL_ERROR) {
        content.remove("id");
        db.update(App.TABLE_NAME, content, String.format("packageName='%s'", app.packageName()));
        return getAppId(db, app.packageName());
      }
      return appId;
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

  public Observable<List<App>> userApps(BriteDatabase db) {
    CursorToListMapper<App> cursorToList =
        new CursorToListMapper<>(App.FACTORY.selectUserAppsMapper());

    return db.createQuery(App.TABLE_NAME, App.SELECTUSERAPPS,
        String.valueOf(ApplicationInfo.FLAG_SYSTEM))
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<App>> byLibrary(BriteDatabase db, Long libraryId) {
    CursorToListMapper<App> cursorToList =
        new CursorToListMapper<>(App.FACTORY.selectByLibraryMapper());

    return db.createQuery(App.TABLE_NAME, App.SELECTBYLIBRARY, String.valueOf(libraryId))
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<App.Info>> allInfos(BriteDatabase db) {
    CursorToListMapper<App.Info> cursorToList = new CursorToListMapper<>(App.Info.MAPPER);

    return db.createQuery(App.TABLE_NAME, App.SELECTALLINFOS)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  private long getAppId(BriteDatabase db, String packageName) {
    Cursor cursor = db.query(App.SELECTAPPID, packageName);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      return cursor.getLong(0);
    }
    return SQL_ERROR;
  }
}
