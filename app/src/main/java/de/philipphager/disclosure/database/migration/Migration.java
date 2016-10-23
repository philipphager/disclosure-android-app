package de.philipphager.disclosure.database.migration;

import android.database.sqlite.SQLiteDatabase;

public interface Migration {
  void update(SQLiteDatabase db);
}
