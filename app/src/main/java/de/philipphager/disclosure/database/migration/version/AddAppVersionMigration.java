package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.version.model.Version;

public class AddAppVersionMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    db.execSQL(Version.CREATE_TABLE);
  }
}
