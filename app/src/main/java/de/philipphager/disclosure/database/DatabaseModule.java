package de.philipphager.disclosure.database;

import android.content.Context;
import android.util.SparseArray;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.app.AppModule;
import de.philipphager.disclosure.database.feature.FeatureModule;
import de.philipphager.disclosure.database.library.modules.LibraryAppModule;
import de.philipphager.disclosure.database.library.modules.LibraryFeatureModule;
import de.philipphager.disclosure.database.library.modules.LibraryModule;
import de.philipphager.disclosure.database.library.populator.LibraryPopulator;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryDescriptionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryFeaturesMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryMigration;
import de.philipphager.disclosure.database.migration.version.AddVersionNameMigration;
import de.philipphager.disclosure.database.version.VersionModule;
import javax.inject.Singleton;

@Module(includes = {
    AppModule.class,
    FeatureModule.class,
    LibraryAppModule.class,
    LibraryModule.class,
    VersionModule.class,
    LibraryFeatureModule.class
})
public class DatabaseModule {
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
    migrations.put(14, AddLibraryDescriptionMigration.class);
    migrations.put(15, AddLibraryMigration.class);
    migrations.put(19, AddLibraryMigration.class);
    migrations.put(22, AddLibraryFeaturesMigration.class);
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
