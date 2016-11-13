package de.philipphager.disclosure.database.version.mapper;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.util.Mapper;
import de.philipphager.disclosure.util.time.Clock;

public class ToVersionMapper implements Mapper<PackageInfo, Version> {
  private final Clock clock;
  private final long appId;

  public ToVersionMapper(Clock clock, long appId) {
    this.clock = clock;
    this.appId = appId;
  }

  @Override public Version map(PackageInfo from) {
    return Version.create(appId, from.versionCode, from.versionName, clock.now());
  }
}
