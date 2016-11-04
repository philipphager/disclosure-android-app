package de.philipphager.disclosure.database.app.mapper;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.Mapper;
import javax.inject.Inject;

public class ToInfoMapper implements Mapper<PackageInfo, App.Info> {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public ToInfoMapper() {
    // Needed for dagger injection.
  }

  @Override public App.Info map(PackageInfo from) {
    return App.Info.create(from.packageName, from.versionCode);
  }
}
