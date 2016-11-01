package de.philipphager.disclosure.database.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.database.util.query.BriteQuery;
import de.philipphager.disclosure.database.util.query.SQLQuery;
import de.philipphager.disclosure.database.util.query.SQLSelector;
import de.philipphager.disclosure.database.util.repository.Repository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository implements Repository<App> {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public AppRepository() {
    // Needed for dagger injection.
  }

  @Override public long add(BriteDatabase db, App app) {
    synchronized (this) {
      ContentValues appContent = App.FACTORY.marshal(app).asContentValues();
      long appId = db.insert(App.TABLE_NAME, appContent, SQLiteDatabase.CONFLICT_IGNORE);

      if (appId == SQL_ERROR) {
        // If the app already exists, return it's id.
        update(db, app);
        return loadAppId(db, app.packageName());
      }
      return appId;
    }
  }

  @Override public int update(BriteDatabase db, App app) {
    ContentValues appContent = App.FACTORY.marshal(app).asContentValues();
    return db.update(App.TABLE_NAME, appContent, String.format("%s=%s", App.ID, app.id()));
  }

  @Override public int remove(BriteDatabase db, SQLSelector selector) {
    return db.delete(App.TABLE_NAME, selector.create());
  }

  @Override public List<App> query(BriteDatabase db, SQLQuery<App> query) {
    CursorToListMapper<App> cursorToListMapper = new CursorToListMapper<>(query.rowMapper());
    Cursor cursor = db.query(query.create());
    return cursorToListMapper.call(cursor);
  }

  @Override public Observable<List<App>> query(BriteDatabase db, BriteQuery<App> query) {
    CursorToListMapper<App> cursorToAppList = new CursorToListMapper<>(query.rowMapper());
    return query.create(db).map(SqlBrite.Query::run).map(cursorToAppList);
  }

  private long loadAppId(BriteDatabase database, String packageName) {
    Cursor cursor = database.query(App.SELECTBYPACKAGE, packageName);

    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      return cursor.getLong(0);
    }
    return SQL_ERROR;
  }
}
