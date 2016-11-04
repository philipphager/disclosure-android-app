package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.library.populator.LibraryPopulator;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.version.model.Version;

public class AddLibraryMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    String dropTable = "DROP TABLE IF EXISTS";
    db.execSQL(String.format("%s %s", dropTable, LibraryApp.TABLE_NAME));
    db.execSQL(String.format("%s %s", dropTable, Library.TABLE_NAME));
    db.execSQL(String.format("%s %s", dropTable, Version.TABLE_NAME));
    db.execSQL(String.format("%s %s", dropTable, App.TABLE_NAME));

    db.execSQL(App.CREATE_TABLE);
    db.execSQL(Version.CREATE_TABLE);
    db.execSQL(Library.CREATE_TABLE);
    db.execSQL(LibraryApp.CREATE_TABLE);
    new LibraryPopulator().populate(db);
  }
}
