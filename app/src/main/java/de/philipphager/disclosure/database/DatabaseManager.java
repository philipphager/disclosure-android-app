package de.philipphager.disclosure.database;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class DatabaseManager {
  private final DatabaseOpenHelper openHelper;
  private final SqlBrite sqlBrite;
  private BriteDatabase readableDB;
  private SQLiteDatabase writeableDB;

  @Inject public DatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    this.openHelper = openHelper;
    this.sqlBrite = sqlBrite;
  }

  public BriteDatabase openReadable() {
    synchronized (this) {
      ensureDatabase();
      return readableDB;
    }
  }

  public SQLiteDatabase openWriteable() {
    synchronized (this) {
      ensureDatabase();
      return writeableDB;
    }
  }

  public void close() {
    synchronized (this) {
      if (readableDB != null) {
        readableDB.close();
        readableDB = null;
      }

      if (writeableDB != null) {
        writeableDB.close();
        writeableDB = null;
      }
    }
  }

  private void ensureDatabase() {
    if (readableDB == null) {
      readableDB = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    }

    if (writeableDB == null) {
      writeableDB = openHelper.getWritableDatabase();
    }
  }
}
