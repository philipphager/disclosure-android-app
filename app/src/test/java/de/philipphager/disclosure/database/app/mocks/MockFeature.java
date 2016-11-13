package de.philipphager.disclosure.database.app.mocks;

import de.philipphager.disclosure.database.feature.model.Feature;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public final class MockFeature {
  public static final Feature TEST = Feature.create(
      "test-id",
      "A/B test",
      "Test multiple versions of features",
      OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
      OffsetDateTime.of(2016, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC));

  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockFeature() {
    //No instances of helper classes.
  }
}
