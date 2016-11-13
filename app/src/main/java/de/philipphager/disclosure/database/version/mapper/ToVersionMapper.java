package de.philipphager.disclosure.database.version.mapper;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.util.Mapper;
import de.philipphager.disclosure.util.time.Now;

public class ToVersionMapper implements Mapper<PackageInfo, Version> {
  private final Now now;
  private final long appId;

  public ToVersionMapper(Now now, long appId) {
    this.now = now;
    this.appId = appId;
  }

  @Override public Version map(PackageInfo from) {
    return Version.create(appId, from.versionCode, from.versionName, now.get());
  }
}
