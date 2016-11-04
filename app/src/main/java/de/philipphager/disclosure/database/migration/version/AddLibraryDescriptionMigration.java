package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.library.populator.LibraryPopulator;
import de.philipphager.disclosure.database.migration.Migration;

public class AddLibraryDescriptionMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + LibraryApp.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + Library.TABLE_NAME);

    db.execSQL(Library.CREATE_TABLE);
    db.execSQL(LibraryApp.CREATE_TABLE);
    new LibraryPopulator().populate(db);
  }
}
