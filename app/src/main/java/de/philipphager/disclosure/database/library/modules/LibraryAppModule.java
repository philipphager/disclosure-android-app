package de.philipphager.disclosure.database.library.modules;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import javax.inject.Singleton;

@Module public class LibraryAppModule {
  @Provides @Singleton
  public LibraryApp.InsertLibraryApp getInsertLibraryAppStatement(DatabaseManager databaseManager) {
    return new LibraryApp.InsertLibraryApp(databaseManager.getSQLite());
  }
}
