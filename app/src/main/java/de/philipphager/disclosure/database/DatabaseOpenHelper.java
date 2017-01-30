package de.philipphager.disclosure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.InitialSchemaMigration;
import javax.inject.Inject;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
  private static final String DB_NAME = "disclosure.db";
  private static final int DB_VERSION = 30;
  private final Migrator migrator;
  private final InitialSchemaMigration initialSchemaMigration;

  @Inject public DatabaseOpenHelper(Context context,
      Migrator migrator,
      InitialSchemaMigration initialSchemaMigration) {
    super(context, DB_NAME, null, DB_VERSION);
    this.migrator = migrator;
    this.initialSchemaMigration = initialSchemaMigration;
  }

  @Override public void onCreate(SQLiteDatabase db) {
    // Initial SQL schema creation
    initialSchemaMigration.update(db);
  }

  @Override public void onConfigure(SQLiteDatabase db) {
    db.setForeignKeyConstraintsEnabled(true);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    migrator.migrate(db, oldVersion, newVersion);
  }
}
