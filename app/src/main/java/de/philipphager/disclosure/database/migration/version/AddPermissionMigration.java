package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.permission.model.PermissionGroup;
import de.philipphager.disclosure.database.permission.populator.PermissionPopulator;

public class AddPermissionMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    String dropTable = "DROP TABLE IF EXISTS ";
    db.execSQL(dropTable + AppPermission.TABLE_NAME);
    db.execSQL(dropTable + PermissionGroup.TABLE_NAME);
    db.execSQL(dropTable + Permission.TABLE_NAME);
    db.execSQL(PermissionGroup.CREATE_TABLE);
    db.execSQL(Permission.CREATE_TABLE);
    db.execSQL(AppPermission.CREATE_TABLE);
    new PermissionPopulator().populate(db);
  }
}
