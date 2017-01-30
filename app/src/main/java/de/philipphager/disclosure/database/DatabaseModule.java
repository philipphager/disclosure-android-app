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
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryDescriptionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryFeaturesMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryMigration;
import de.philipphager.disclosure.database.migration.version.AddPermissionMigration;
import de.philipphager.disclosure.database.migration.version.AddVersionNameMigration;
import de.philipphager.disclosure.database.migration.version.InitialSchemaMigration;
import de.philipphager.disclosure.database.permission.PermissionModule;
import de.philipphager.disclosure.database.version.VersionModule;
import javax.inject.Singleton;

@Module(includes = {
    AppModule.class,
    FeatureModule.class,
    LibraryAppModule.class,
    LibraryModule.class,
    PermissionModule.class,
    VersionModule.class,
    LibraryFeatureModule.class
})
public class DatabaseModule {
  @Provides @Singleton
  public DatabaseOpenHelper getDataBaseOpenHelper(Context context, Migrator migrator,
      InitialSchemaMigration initialSchemaMigration) {
    return new DatabaseOpenHelper(context, migrator, initialSchemaMigration);
  }

  @Provides @Singleton public Migrator getMigrator() {
    SparseArray<Class<? extends Migration>> migrations = new SparseArray<>();
    migrations.put(3, AddAppVersionMigration.class);
    migrations.put(4, AddVersionNameMigration.class);
    migrations.put(11, AddLibraryMigration.class);
    migrations.put(14, AddLibraryDescriptionMigration.class);
    migrations.put(15, AddLibraryMigration.class);
    migrations.put(19, AddLibraryMigration.class);
    migrations.put(25, AddLibraryFeaturesMigration.class);
    migrations.put(30, AddPermissionMigration.class);
    migrations.put(31, AddLibraryMigration.class);
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
