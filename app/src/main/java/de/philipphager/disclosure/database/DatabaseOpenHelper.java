package de.philipphager.disclosure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import de.philipphager.disclosure.database.migration.Migrator;
import javax.inject.Inject;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
  private static final String DB_NAME = "disclosure.db";
  private static final int DB_VERSION = 1;
  private final Migrator migrator;

  @Inject public DatabaseOpenHelper(Context context, Migrator migrator) {
    super(context, DB_NAME, null, DB_VERSION);

    this.migrator = migrator;
  }

  @Override public void onOpen(SQLiteDatabase db) {
    super.onOpen(db);

    db.setForeignKeyConstraintsEnabled(true);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    migrator.migrate(db, oldVersion, newVersion);
  }
}
