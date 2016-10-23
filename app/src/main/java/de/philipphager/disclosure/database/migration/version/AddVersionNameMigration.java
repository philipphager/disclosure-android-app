package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.version.model.Version;

public class AddVersionNameMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + Version.TABLE_NAME);
    db.execSQL(Version.CREATE_TABLE);
  }
}
