package de.philipphager.disclosure.database.app;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorListMapper;
import de.philipphager.disclosure.database.util.Repository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository implements Repository<App> {
  private final DatabaseManager databaseManager;

  @Inject public AppRepository(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  @Override public void add(App app) {
    try (BriteDatabase db = databaseManager.open()) {
      db.insert(App.TABLE_NAME, App.FACTORY.marshal(app).asContentValues());
    }
  }

  @Override public void add(Iterable<App> apps) {
    try (BriteDatabase db = databaseManager.open();
         BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (App app : apps) {
        db.insert(App.TABLE_NAME, App.FACTORY.marshal(app).asContentValues());
      }
      transaction.markSuccessful();
    }
  }

  @Override public void update(App app) {
    try (BriteDatabase db = databaseManager.open()) {
      db.update(App.TABLE_NAME, App.FACTORY.marshal(app).asContentValues(),
          String.format("%s=%s", App.ID, app.id()));
    }
  }

  @Override public void remove(App app) {
    try (BriteDatabase db = databaseManager.open()) {
      db.delete(App.TABLE_NAME, String.format("%s=%s", App.ID, app.id()));
    }
  }

  @Override public Observable<List<App>> query(BriteQuery<App> query) {
    try (BriteDatabase db = databaseManager.open()) {

      CursorListMapper<App> appListMapper = new CursorListMapper<>(query.rowMapper());
      return query.createQuery(db).map(appListMapper);
    }
  }
}
