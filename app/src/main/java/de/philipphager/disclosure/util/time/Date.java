package de.philipphager.disclosure.util.time;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public final class Date {
  public static OffsetDateTime MIN = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

  private Date() {
    // No instances.
  }
}
