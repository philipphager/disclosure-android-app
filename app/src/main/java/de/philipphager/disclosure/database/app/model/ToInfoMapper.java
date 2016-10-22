package de.philipphager.disclosure.database.app.model;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.util.Mapper;
import javax.inject.Inject;

public class ToInfoMapper implements Mapper<PackageInfo, App.Info> {
  @Inject public ToInfoMapper() {
  }

  @Override public App.Info map(PackageInfo from) {
    return App.Info.create(from.packageName, from.versionCode);
  }
}
