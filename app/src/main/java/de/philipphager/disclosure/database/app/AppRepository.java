package de.philipphager.disclosure.database.app;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Repository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository implements Repository<App> {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public AppRepository() {
    // Needed for dagger injection.
  }

  @Override public long add(SQLiteDatabase db, App app) {
    synchronized (this) {
      return db.replace(App.TABLE_NAME, null, App.FACTORY.marshal(app).asContentValues());
    }
  }

  @Override public void add(SQLiteDatabase db, Iterable<App> apps) {
    for (App app : apps) {
      db.insert(App.TABLE_NAME, null, App.FACTORY.marshal(app).asContentValues());
    }
  }

  @Override public void update(SQLiteDatabase db, App app) {
    db.update(App.TABLE_NAME, App.FACTORY.marshal(app).asContentValues(),
        String.format("%s=%s", App.ID, app.id()), null);
  }

  @Override public void remove(SQLiteDatabase db, App app) {
    db.delete(App.TABLE_NAME, String.format("%s=%s", App.ID, app.id()), null);
  }

  public void remove(SQLiteDatabase db, String sql) {
    db.delete(App.TABLE_NAME, sql, null);
  }

  @Override public Observable<List<App>> query(BriteDatabase db, BriteQuery<App> query) {
    CursorToListMapper<App> cursorToAppList = new CursorToListMapper<>(query.rowMapper());
    return query.createQuery(db).map(cursorToAppList);
  }
}
