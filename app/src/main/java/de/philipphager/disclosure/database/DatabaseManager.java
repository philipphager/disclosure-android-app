package de.philipphager.disclosure.database;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class DatabaseManager {
  private final DatabaseOpenHelper openHelper;
  private final SqlBrite sqlBrite;
  private BriteDatabase observableDB;
  private SQLiteDatabase writeableDB;

  @Inject public DatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    this.openHelper = openHelper;
    this.sqlBrite = sqlBrite;
  }

  public SQLiteDatabase openWriteable() {
    synchronized (this) {
      if (writeableDB == null) {
        writeableDB = openHelper.getWritableDatabase();
      }
      return writeableDB;
    }
  }

  public void closeWriteable() {
    synchronized (this) {
      if (writeableDB != null) {
        writeableDB.close();
        writeableDB = null;
      }
    }
  }

  public BriteDatabase openObservable() {
    synchronized (this) {
      if (observableDB == null) {
        observableDB = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
      }
      return observableDB;
    }
  }

  public void closeObservable() {
    synchronized (this) {
      if (observableDB != null) {
        observableDB.close();
        observableDB = null;
      }
    }
  }
}
