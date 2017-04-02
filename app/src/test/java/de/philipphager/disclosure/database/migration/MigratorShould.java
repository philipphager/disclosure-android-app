package de.philipphager.disclosure.database.migration;

import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MigratorShould {
  @Mock protected SQLiteDatabase database;
  private Migrator migrator;
  private SparseArray<Class<? extends Migration>> migrations;

  @Before public void setUp() {
    migrations = new SparseArray<>();
    migrations.put(1, MockMigration.class);
    migrations.put(2, MockMigration.class);
    migrations.put(3, MockMigration.class);
    migrator = new Migrator(migrations);
  }

  @Test public void runAllExistingMigrationsBetweenVersions() {
    migrator.migrate(database, 0, 3);

    verify(database).beginTransaction();
    verify(database, times(migrations.size())).execSQL(MockMigration.MOCK_SQL);
    verify(database).setTransactionSuccessful();
    verify(database).endTransaction();
  }

  @Test public void ignoreMissingMigrationsBetweenVersions() {
    migrations = new SparseArray<>();
    migrations.put(1, MockMigration.class);
    migrations.put(3, MockMigration.class);
    migrations.put(5, MockMigration.class);

    migrator.migrate(database, 0, 6);

    verify(database).beginTransaction();
    verify(database, times(3)).execSQL(MockMigration.MOCK_SQL);
    verify(database).setTransactionSuccessful();
    verify(database).endTransaction();
  }

  @Test public void runNoMigrationIfNewVersionIsHigherThanLatestMigration() {
    migrator.migrate(database, 5, 7);

    verify(database).beginTransaction();
    verify(database, never()).execSQL(MockMigration.MOCK_SQL);
    verify(database).setTransactionSuccessful();
    verify(database).endTransaction();
  }

  @Test public void runNoMigrationIfVersionsAreEqual() {
    migrator.migrate(database, 1, 1);

    verify(database).beginTransaction();
    verify(database, never()).execSQL(MockMigration.MOCK_SQL);
    verify(database).setTransactionSuccessful();
    verify(database).endTransaction();
  }
}
