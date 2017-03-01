package de.philipphager.disclosure.database.app;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import javax.inject.Singleton;

@Module public class AppModule {
  @Provides @Singleton public App.InsertApp getInsertStatement(DatabaseManager databaseManager) {
    return new App.InsertApp(databaseManager.getSQLite(), App.FACTORY);
  }

  @Provides @Singleton public App.UpdateApp getUpdateStatement(DatabaseManager databaseManager) {
    return new App.UpdateApp(databaseManager.getSQLite(), App.FACTORY);
  }
}
