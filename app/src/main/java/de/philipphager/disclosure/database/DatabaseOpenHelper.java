package de.philipphager.disclosure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.version.model.Version;
import javax.inject.Inject;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
  private static final String DB_NAME = "disclosure.db";
  private static final int DB_VERSION = 4;
  private final Migrator migrator;

  @Inject public DatabaseOpenHelper(Context context, Migrator migrator) {
    super(context, DB_NAME, null, DB_VERSION);
    this.migrator = migrator;
  }

  @Override public void onCreate(SQLiteDatabase db) {
    //Initial SQL schema creation
    db.execSQL(App.CREATE_TABLE);
    db.execSQL(Version.CREATE_TABLE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    migrator.migrate(db, oldVersion, newVersion);
  }
}
