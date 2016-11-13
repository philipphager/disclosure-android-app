package de.philipphager.disclosure.database.version;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.version.model.Version;
import javax.inject.Singleton;

@Module public class VersionModule {
  @Provides @Singleton
  public Version.InsertVersion getInsertVersionStatement(DatabaseManager databaseManager) {
    return new Version.InsertVersion(databaseManager.getSQLite(), Version.FACTORY);
  }

  @Provides @Singleton
  public Version.UpdateVersion getUpdateVersionStatement(DatabaseManager databaseManager) {
    return new Version.UpdateVersion(databaseManager.getSQLite(), Version.FACTORY);
  }
}
