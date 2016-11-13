package de.philipphager.disclosure.database.feature;

import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.feature.model.Feature;
import javax.inject.Singleton;

@Module public class FeatureModule {
  @Provides @Singleton
  public Feature.InsertFeature getInsertFeatureStatement(DatabaseManager databaseManager) {
    return new Feature.InsertFeature(databaseManager.getSQLite(), Feature.FACTORY);
  }

  @Provides @Singleton
  public Feature.UpdateFeature getUpdateFeatureStatement(DatabaseManager databaseManager) {
    return new Feature.UpdateFeature(databaseManager.getSQLite(), Feature.FACTORY);
  }
}
