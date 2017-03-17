package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.permission.model.PermissionGroup;
import de.philipphager.disclosure.database.version.model.Version;
import javax.inject.Inject;

public class InitialSchemaMigration implements Migration {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public InitialSchemaMigration() {
    // Needed for dagger injection.
  }

  @Override public void update(SQLiteDatabase db) {
    db.execSQL(App.CREATE_TABLE);
    db.execSQL(Version.CREATE_TABLE);
    db.execSQL(Library.CREATE_TABLE);
    db.execSQL(LibraryApp.CREATE_TABLE);
    db.execSQL(PermissionGroup.CREATE_TABLE);
    db.execSQL(Permission.CREATE_TABLE);
    db.execSQL(AppPermission.CREATE_TABLE);
  }
}
