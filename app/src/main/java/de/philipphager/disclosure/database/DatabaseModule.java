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
import de.philipphager.disclosure.database.method.modules.ProtectedMethodModule;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppLibraryPermissionMigration;
import de.philipphager.disclosure.database.permission.PermissionModule;
import de.philipphager.disclosure.database.version.VersionModule;
import javax.inject.Singleton;

@Module(includes = {
    AppModule.class,
    FeatureModule.class,
    LibraryAppModule.class,
    LibraryModule.class,
    PermissionModule.class,
    ProtectedMethodModule.class,
    VersionModule.class,
    LibraryFeatureModule.class
})
public class DatabaseModule {
  @Provides @Singleton
  public DatabaseOpenHelper getDataBaseOpenHelper(Context context, Migrator migrator) {
    return new DatabaseOpenHelper(context, migrator);
  }

  @Provides @Singleton public Migrator getMigrator() {
    SparseArray<Class<? extends Migration>> migrations = new SparseArray<>();
    migrations.append(2, AddAppLibraryPermissionMigration.class);
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
