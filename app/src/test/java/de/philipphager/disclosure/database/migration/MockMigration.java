package de.philipphager.disclosure.database.migration;

import android.database.sqlite.SQLiteDatabase;

public class MockMigration implements Migration {
  public static final String MOCK_SQL = "SELECT * FROM MOCK";

  @Override public void update(SQLiteDatabase db) {
    db.execSQL(MOCK_SQL);
  }
}
