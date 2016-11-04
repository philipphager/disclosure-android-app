package de.philipphager.disclosure.database;

import android.content.Context;
import android.util.SparseArray;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.library.populator.LibraryPopulator;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryMigration;
import de.philipphager.disclosure.database.migration.version.AddVersionNameMigration;
import javax.inject.Singleton;

@Module public class DatabaseModule {
  @Provides @Singleton
  public DatabaseOpenHelper getDataBaseOpenHelper(Context context, Migrator migrator,
      LibraryPopulator libraryPopulator) {
    return new DatabaseOpenHelper(context, migrator, libraryPopulator);
  }

  @Provides @Singleton public Migrator getMigrator() {
    SparseArray<Class<? extends Migration>> migrations = new SparseArray<>();
    migrations.put(3, AddAppVersionMigration.class);
    migrations.put(4, AddVersionNameMigration.class);
    migrations.put(11, AddLibraryMigration.class);
    return new Migrator(migrations);
  }

  @Provides @Singleton public SqlBrite getSqlBrite() {
    return new SqlBrite.Builder().build();
  }

  @Provides @Singleton
  public DatabaseManager getDatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    return new DatabaseManager(openHelper, sqlBrite);
  }
}
