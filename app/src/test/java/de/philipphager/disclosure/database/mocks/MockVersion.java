package de.philipphager.disclosure.database.mocks;

import de.philipphager.disclosure.database.version.model.Version;
import org.threeten.bp.LocalDateTime;

public final class MockVersion {
  public static final Version TEST =
      Version.create(MockApp.TEST.id(), 1234, "1234", LocalDateTime.of(2016, 1, 1, 0, 0, 0));

  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockVersion() {
    // No instances of mock classes.
  }
}
