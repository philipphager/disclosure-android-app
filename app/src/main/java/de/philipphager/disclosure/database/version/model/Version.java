package de.philipphager.disclosure.database.version.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.ColumnAdapter;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

@AutoValue public abstract class Version implements VersionModel {
  public static final Factory<Version> FACTORY =
      new Factory<>(Version::create, new ColumnAdapter<LocalDateTime>() {
        @Override public LocalDateTime map(Cursor cursor, int columnIndex) {
          long millis = cursor.getLong(columnIndex);
          return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        @Override public void marshal(ContentValues values, String key, LocalDateTime value) {
          values.put(key, value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
      });

  public static Version create(Long appId, Long versionNumber, LocalDateTime createdAt) {
    return new AutoValue_Version(appId, versionNumber, createdAt);
  }

  public abstract long appId();

  public abstract long versionNumber();

  public abstract LocalDateTime createdAt();
}
