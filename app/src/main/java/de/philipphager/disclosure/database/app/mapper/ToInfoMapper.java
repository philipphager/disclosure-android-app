package de.philipphager.disclosure.database.app.mapper;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.AppInfo;
import de.philipphager.disclosure.util.Mapper;
import javax.inject.Inject;

public class ToInfoMapper implements Mapper<PackageInfo, AppInfo> {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public ToInfoMapper() {
    // Needed for dagger injection.
  }

  @Override public AppInfo map(PackageInfo from) {
    return AppInfo.create(from.packageName, from.versionCode);
  }
}
