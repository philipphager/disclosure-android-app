package de.philipphager.disclosure.database.library.modules;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryModel;
import javax.inject.Singleton;

@Module public class LibraryModule {
  @Provides @Singleton
  public Library.InsertLibrary getInsertLibraryStatement(DatabaseManager databaseManager) {
    return new LibraryModel.InsertLibrary(databaseManager.getSQLite(), Library.FACTORY);
  }

  @Provides @Singleton
  public Library.UpdateLibrary getUpdateLibraryStatement(DatabaseManager databaseManager) {
    return new LibraryModel.UpdateLibrary(databaseManager.getSQLite(), Library.FACTORY);
  }
}
