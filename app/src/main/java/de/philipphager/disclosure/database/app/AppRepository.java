package de.philipphager.disclosure.database.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.util.SQLQuery;
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
      long result = db.insert(App.TABLE_NAME, appContent, SQLiteDatabase.CONFLICT_IGNORE);

      if (result == SQL_ERROR) {
        // App might already exist in the db, try to load app's id.
        return loadAppId(db, app.packageName());
      }
      return result;
    }
  }

  @Override public void add(BriteDatabase db, Iterable<App> apps) {
    for (App app : apps) {
      add(db, app);
    }
  }

  @Override public void update(BriteDatabase db, App app) {
    ContentValues appContent = App.FACTORY.marshal(app).asContentValues();
    db.update(App.TABLE_NAME, appContent, String.format("%s=%s", App.ID, app.id()));
  }

  @Override public void remove(BriteDatabase db, SQLQuery query) {
    db.delete(App.TABLE_NAME, query.toSQL());
  }

  @Override public Observable<List<App>> query(BriteDatabase db, BriteQuery<App> query) {
    CursorToListMapper<App> cursorToAppList = new CursorToListMapper<>(query.rowMapper());
    return query.createQuery(db).map(cursorToAppList);
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
