package de.philipphager.disclosure.database;

import android.content.Context;
import android.util.SparseArray;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.library.model.LibraryModel;
import de.philipphager.disclosure.database.library.populator.LibraryPopulator;
import de.philipphager.disclosure.database.migration.Migration;
import de.philipphager.disclosure.database.migration.Migrator;
import de.philipphager.disclosure.database.migration.version.AddAppVersionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryDescriptionMigration;
import de.philipphager.disclosure.database.migration.version.AddLibraryMigration;
import de.philipphager.disclosure.database.migration.version.AddVersionNameMigration;
import de.philipphager.disclosure.database.version.model.Version;
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
    migrations.put(14, AddLibraryDescriptionMigration.class);
    migrations.put(15, AddLibraryMigration.class);
    migrations.put(19, AddLibraryMigration.class);
    return new Migrator(migrations);
  }

  @Provides @Singleton public SqlBrite getSqlBrite() {
    return new SqlBrite.Builder().build();
  }

  @Provides @Singleton
  public DatabaseManager getDatabaseManager(DatabaseOpenHelper openHelper, SqlBrite sqlBrite) {
    return new DatabaseManager(openHelper, sqlBrite);
  }

  @Provides @Singleton
  public Library.InsertLibrary getInsertLibraryStatement(DatabaseManager databaseManager) {
    return new LibraryModel.InsertLibrary(databaseManager.getSQLite(), Library.FACTORY);
  }

  @Provides @Singleton
  public Library.UpdateLibrary getUpdateLibraryStatement(DatabaseManager databaseManager) {
    return new LibraryModel.UpdateLibrary(databaseManager.getSQLite(), Library.FACTORY);
  }

  @Provides @Singleton
  public LibraryApp.InsertForApp getInsertLibraryForAppStatement(DatabaseManager databaseManager) {
    return new LibraryApp.InsertForApp(databaseManager.getSQLite());
  }

  @Provides @Singleton
  public App.InsertApp getInsertAppStatement(DatabaseManager databaseManager) {
    return new App.InsertApp(databaseManager.getSQLite());
  }

  @Provides @Singleton
  public App.UpdateApp getUpdateAppStatement(DatabaseManager databaseManager) {
    return new App.UpdateApp(databaseManager.getSQLite());
  }

  @Provides @Singleton
  public Version.InsertVersion getInsertVersionStatement(DatabaseManager databaseManager) {
    return new Version.InsertVersion(databaseManager.getSQLite(), Version.FACTORY);
  }

  @Provides @Singleton
  public Version.UpdateVersion getUpdateVersionStatement(DatabaseManager databaseManager) {
    return new Version.UpdateVersion(databaseManager.getSQLite(), Version.FACTORY);
  }
}
