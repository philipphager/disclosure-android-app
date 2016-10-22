package de.philipphager.disclosure.database;

import android.content.Context;
import android.util.SparseArray;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import de.philipphager.disclosure.database.migration.version.AddVersionNameMigration;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.VersionRepository;
import de.philipphager.disclosure.database.version.model.Version;
import javax.inject.Singleton;

@Module public class DatabaseModule {
  @Provides @Singleton
  public DatabaseOpenHelper getDataBaseOpenHelper(Context context, Migrator migrator) {
    return new DatabaseOpenHelper(context, migrator);
  }

  @Provides @Singleton public Migrator getMigrator() {
    SparseArray<Class<? extends Migration>> migrations = new SparseArray<>();
    migrations.put(3, AddAppVersionMigration.class);
    migrations.put(4, AddVersionNameMigration.class);
    return new Migrator(migrations);
  }

  @Provides @Singleton public SqlBrite getSqlBrite() {
    return SqlBrite.create();
  }

  @Provides public Repository<App> getAppRepository(DatabaseManager databaseManager) {
    return new AppRepository(databaseManager);
  }

  @Provides public Repository<Version> getVersionRepository(DatabaseManager databaseManager) {
    return new VersionRepository(databaseManager);
  }
}
