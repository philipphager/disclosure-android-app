package de.philipphager.disclosure.database.app;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Repository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository implements Repository<App> {
  private final DatabaseManager databaseManager;

  @Inject public AppRepository(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  @Override public long add(App app) {
    synchronized (this) {
      SQLiteDatabase db = databaseManager.openWriteable();
      return db.replace(App.TABLE_NAME, null, App.FACTORY.marshal(app).asContentValues());
    }
  }

  @Override public void add(Iterable<App> apps) {
    BriteDatabase db = databaseManager.openReadable();
    BriteDatabase.Transaction transaction = db.newTransaction();

    for (App app : apps) {
      db.insert(App.TABLE_NAME, App.FACTORY.marshal(app).asContentValues());
    }
    transaction.markSuccessful();
  }

  @Override public void update(App app) {
    BriteDatabase db = databaseManager.openReadable();
    db.update(App.TABLE_NAME, App.FACTORY.marshal(app).asContentValues(),
        String.format("%s=%s", App.ID, app.id()));
  }

  @Override public void remove(App app) {
    BriteDatabase db = databaseManager.openReadable();
    db.delete(App.TABLE_NAME, String.format("%s=%s", App.ID, app.id()));
  }

  public void remove(String sql) {
    BriteDatabase db = databaseManager.openReadable();
    db.delete(App.TABLE_NAME, sql);
  }

  @Override public Observable<List<App>> query(BriteQuery<App> query) {
    BriteDatabase db = databaseManager.openReadable();
    CursorToListMapper<App> cursorToAppList = new CursorToListMapper<>(query.rowMapper());
    return query.createQuery(db).map(cursorToAppList);
  }

  public Observable<List<App.Info>> allInfos() {
    BriteDatabase db = databaseManager.openReadable();
    CursorToListMapper<App.Info> cursorToInfoList = new CursorToListMapper<>(App.Info.MAPPER);

    return db.createQuery(App.TABLE_NAME, App.SELECTALLINFOS, new String[0]).map(cursorToInfoList);
  }
}
