package de.philipphager.disclosure.database.version.mapper;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.util.Mapper;
import org.threeten.bp.LocalDateTime;

public class ToVersionMapper implements Mapper<PackageInfo, Version> {
  private final long appId;

  public ToVersionMapper(long appId) {
    this.appId = appId;
  }

  @Override public Version map(PackageInfo from) {
    return Version.create(appId, from.versionCode, from.versionName, LocalDateTime.now());
  }
}
