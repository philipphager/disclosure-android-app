package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.permission.model.AppLibraryPermission;

public class AddAppLibraryPermissionMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    String dropTable = "DROP TABLE IF EXISTS ";
    db.execSQL(dropTable + AppLibraryPermission.TABLE_NAME);
    db.execSQL(AppLibraryPermission.CREATE_TABLE);
  }
}
