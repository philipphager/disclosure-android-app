package de.philipphager.disclosure.database.method.modules;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.method.model.ProtectedMethodModel;
import javax.inject.Singleton;

@Module public class ProtectedMethodModule {
  @Provides @Singleton
  public ProtectedMethodModel.InsertMethod getInsertMethod(DatabaseManager databaseManager) {
    return new ProtectedMethodModel.InsertMethod(databaseManager.getSQLite());
  }
}
