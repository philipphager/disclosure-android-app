package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.feature.model.Feature;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.version.model.Version;

public class AddLibraryFeaturesMigration implements Migration {
  @Override public void update(SQLiteDatabase db) {
    String dropTable = "DROP TABLE IF EXISTS ";
    db.execSQL(dropTable + App.TABLE_NAME);
    db.execSQL(dropTable + LibraryApp.TABLE_NAME);
    db.execSQL(dropTable + LibraryFeature.TABLE_NAME);
    db.execSQL(dropTable + Version.TABLE_NAME);
    db.execSQL(dropTable + Library.TABLE_NAME);
    db.execSQL(dropTable + Feature.TABLE_NAME);

    db.execSQL(App.CREATE_TABLE);
    db.execSQL(Version.CREATE_TABLE);
    db.execSQL(Library.CREATE_TABLE);
    db.execSQL(LibraryApp.CREATE_TABLE);
    db.execSQL(Feature.CREATE_TABLE);
    db.execSQL(LibraryFeature.CREATE_TABLE);
  }
}
