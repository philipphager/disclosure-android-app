package de.philipphager.disclosure.util.time;

import javax.inject.Inject;
import org.threeten.bp.LocalDateTime;

public class Now {
  @SuppressWarnings("PMD.UnnecessaryConstructor") @Inject public Now() {
    // Needed for dagger injection.
  }

  public LocalDateTime get() {
    return LocalDateTime.now();
  }
}
