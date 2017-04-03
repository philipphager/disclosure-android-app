package de.philipphager.disclosure.database.version.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import de.philipphager.disclosure.database.util.adapters.LocalDateTimeColumnAdapter;
import org.threeten.bp.LocalDateTime;

@AutoValue public abstract class Version implements VersionModel, Parcelable {
  public static final Factory<Version> FACTORY =
      new Factory<>(Version::create, new LocalDateTimeColumnAdapter());

  public static Version create(Long appId, int versionCode, String versionName,
      LocalDateTime createdAt) {
    return new AutoValue_Version(appId, versionCode, versionName, createdAt);
  }

  public abstract long appId();

  public abstract int versionCode();

  @Nullable public abstract String versionName();

  public abstract LocalDateTime createdAt();
}
