package de.philipphager.disclosure.database;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class DatabaseManager {
  private final DatabaseOpenHelper openHelper;
  private final SqlBrite sqlBrite;
  private SQLiteDatabase db;
  private BriteDatabase observableDB;

  @Inject public DatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    this.openHelper = openHelper;
    this.sqlBrite = sqlBrite;
  }

  public BriteDatabase get() {
    synchronized (this) {
      if (observableDB == null) {
        observableDB = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
      }
      return observableDB;
    }
  }

  public SQLiteDatabase getSQLite() {
    synchronized (this) {
      if (db == null) {
        db = openHelper.getWritableDatabase();
      }
      return db;
    }
  }
}
