package de.philipphager.disclosure.database.util.mapper.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import com.squareup.sqldelight.ColumnAdapter;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

public class instantColumnAdapter implements ColumnAdapter<LocalDateTime> {

  @Override public LocalDateTime map(Cursor cursor, int columnIndex) {
    long millis = cursor.getLong(columnIndex);
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  @Override public void marshal(ContentValues values, String key, LocalDateTime value) {
    values.put(key, value.ut.toInstant().toEpochMilli());
  }
}
