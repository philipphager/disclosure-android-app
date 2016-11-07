package de.philipphager.disclosure.database.util.mapper.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import com.squareup.sqldelight.ColumnAdapter;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public class LocalDateTimeColumnAdapter implements ColumnAdapter<OffsetDateTime> {
  @Override public OffsetDateTime map(Cursor cursor, int columnIndex) {
    long millis = cursor.getLong(columnIndex);
    return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC);
  }

  @Override public void marshal(ContentValues values, String key, OffsetDateTime value) {
    values.put(key, value.toInstant().toEpochMilli());
  }
}
