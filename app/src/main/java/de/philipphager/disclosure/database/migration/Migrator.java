package de.philipphager.disclosure.database.migration;

import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.check;

public class Migrator {
  private final SparseArray<Class<? extends Migration>> migrations;

  @Inject public Migrator(SparseArray<Class<? extends Migration>> migrations) {
    this.migrations = migrations;
  }

  public void migrate(SQLiteDatabase db, int oldVersion, int newVersion) {
    //check(oldVersion <= newVersion, "invalid database versions");

    db.beginTransaction();

    try {
      List<Migration> migrations = getMigrations(oldVersion, newVersion);
      for (Migration migration : migrations) {
        migration.update(db);
        String name = migration.getClass().getName();
        Timber.d("Finished migration %s", name);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  private List<Migration> getMigrations(int oldVersion, int newVersion) {
    int size = newVersion - oldVersion;
    List<Migration> inflatedMigrations = new ArrayList<>(size > 0 ? size : 0);

    for (int i = oldVersion + 1; i <= newVersion; i++) {
      Class<? extends Migration> migrationClass = this.migrations.get(i);

      if (migrationClass == null) {
        continue;
      }

      Migration migration;
      try {
        migration = migrationClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new IllegalArgumentException(
            "Migration class needs empty and accessible constructor!", e);
      }

      if (migration != null) {
        inflatedMigrations.add(migration);
      }
    }

    return inflatedMigrations;
  }
}
