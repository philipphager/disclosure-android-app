package de.philipphager.disclosure.database.feature;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.app.mocks.MockFeature;
import de.philipphager.disclosure.database.feature.model.Feature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@PrepareForTest({
    BriteDatabase.class,
    Feature.InsertFeature.class,
    Feature.UpdateFeature.class
})
public class FeatureRepositoryShould {
  protected BriteDatabase database;
  protected FeatureRepository featureRepository;
  protected Feature.InsertFeature insertFeature;
  protected Feature.UpdateFeature updateFeature;

  @Before public void setUp() {
    insertFeature = PowerMockito.mock(Feature.InsertFeature.class);
    updateFeature = PowerMockito.mock(Feature.UpdateFeature.class);
    database = PowerMockito.mock(BriteDatabase.class);
    featureRepository = new FeatureRepository(insertFeature, updateFeature);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertFeatureIntoDatabase() {
    Feature feature = MockFeature.TEST;
    featureRepository.insert(database, feature);

    verify(insertFeature).bind(feature.id(),
        feature.title(),
        feature.description(),
        feature.createdAt(),
        feature.updatedAt());
    verify(database).executeInsert(insertFeature.table, insertFeature.program);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void updateFeatureInDatabase() {
    Feature feature = MockFeature.TEST;
    featureRepository.update(database, feature);

    verify(updateFeature).bind(feature.title(),
        feature.description(),
        feature.createdAt(),
        feature.updatedAt(),
        feature.id());
    verify(database).executeUpdateDelete(updateFeature.table, updateFeature.program);
  }
}

