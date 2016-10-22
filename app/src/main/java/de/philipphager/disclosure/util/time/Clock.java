package de.philipphager.disclosure.util.time;

import javax.inject.Inject;
import org.threeten.bp.LocalDateTime;

public class Clock {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public Clock() {
    // Needed for dagger injection.
  }

  public LocalDateTime now() {
    return LocalDateTime.now();
  }
}
