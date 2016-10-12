package de.philipphager.disclosure.database;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class DatabaseManager {
  private final DatabaseOpenHelper openHelper;
  private final SqlBrite sqlBrite;
  private BriteDatabase database;

  @Inject public DatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    this.openHelper = openHelper;
    this.sqlBrite = sqlBrite;
  }

  public synchronized BriteDatabase open() {
    ensureDatabase();
    return database;
  }

  public synchronized void close() {
    if (database != null) {
      database.close();
      database = null;
    }
  }

  private void ensureDatabase() {
    if (database == null) {
      database = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
      ;
    }
  }
}
