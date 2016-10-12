package de.philipphager.disclosure.database;

import android.content.Context;
import android.util.SparseArray;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import javax.inject.Singleton;

@Module public class DatabaseModule {
  @Provides @Singleton
  public DatabaseOpenHelper getDataBaseOpenHelper(Context context, Migrator migrator) {
    return new DatabaseOpenHelper(context, migrator);
  }

  @Provides @Singleton public Migrator getMigrator() {
    SparseArray<Class<? extends Migration>> migrations = new SparseArray<>();
    migrations.put(3, AddAppVersionMigration.class);
    return new Migrator(migrations);
  }

  @Provides @Singleton public SqlBrite getSqlBrite() {
    return SqlBrite.create();
  }
}
