package de.philipphager.disclosure.database.util.adapters;

import android.support.annotation.NonNull;
import com.squareup.sqldelight.ColumnAdapter;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

public class LocalDateTimeColumnAdapter implements ColumnAdapter<LocalDateTime, Long> {
  @NonNull @Override public LocalDateTime decode(Long databaseValue) {
    return Instant.ofEpochMilli(databaseValue).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  @Override public Long encode(@NonNull LocalDateTime value) {
    return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}
