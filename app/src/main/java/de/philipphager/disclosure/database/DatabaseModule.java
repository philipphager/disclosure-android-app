package de.philipphager.disclosure.database;

import android.content.Context;
import android.util.SparseArray;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.info.AppInfoRepository;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import de.philipphager.disclosure.database.migration.version.AddVersionNameMigration;
import de.philipphager.disclosure.database.util.Queryable;
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
    return new SqlBrite.Builder().build();
  }

  @Provides public Repository<App> getAppRepository() {
    return new AppRepository();
  }

  @Provides public Repository<Version> getVersionRepository() {
    return new VersionRepository();
  }

  @Provides public Queryable<App.Info> getAppInfoRepository() {
    return new AppInfoRepository();
  }

  @Provides @Singleton
  public DatabaseManager getDatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    return new DatabaseManager(openHelper, sqlBrite);
  }
}
