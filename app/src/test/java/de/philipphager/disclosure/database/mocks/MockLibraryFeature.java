package de.philipphager.disclosure.database.mocks;

import de.philipphager.disclosure.database.library.model.LibraryFeature;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public final class MockLibraryFeature {
  public static final LibraryFeature TEST = LibraryFeature.create(
      "test-id",
      "test-library-id",
      "test-feature-id",
      OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
      OffsetDateTime.of(2016, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC));

  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockLibraryFeature() {
    // No instances of mock classes.
  }
}
