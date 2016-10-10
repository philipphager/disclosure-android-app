package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.model.Version;
import de.philipphager.disclosure.database.migration.Migration;

public class AddAppVersionMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    db.execSQL(Version.CREATE_TABLE);
  }
}
