package de.philipphager.disclosure.database.util.adapters;

import android.support.annotation.NonNull;
import com.squareup.sqldelight.ColumnAdapter;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public class OffsetDateTimeColumnAdapter implements ColumnAdapter<OffsetDateTime, Long> {
  @NonNull @Override public OffsetDateTime decode(Long databaseValue) {
    return Instant.ofEpochMilli(databaseValue).atOffset(ZoneOffset.UTC);
  }

  @Override public Long encode(@NonNull OffsetDateTime value) {
    return value.toInstant().toEpochMilli();
  }
}
