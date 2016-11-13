package de.philipphager.disclosure.database.library.modules;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import javax.inject.Singleton;

@Module public class LibraryFeatureModule {
  @Provides @Singleton
  public LibraryFeature.InsertLibraryFeature getInsertFeatureForLibraryStatement(
      DatabaseManager databaseManager) {
    return new LibraryFeature.InsertLibraryFeature(databaseManager.getSQLite(),
        LibraryFeature.FACTORY);
  }

  @Provides @Singleton
  public LibraryFeature.UpdateLibraryFeature getUpdateFeatureForLibraryStatement(
      DatabaseManager databaseManager) {
    return new LibraryFeature.UpdateLibraryFeature(databaseManager.getSQLite(),
        LibraryFeature.FACTORY);
  }
}
