package de.philipphager.disclosure.database.app.mapper;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.Mapper;
import javax.inject.Inject;

public class ToAppMapper implements Mapper<ApplicationInfo, App> {
  private final PackageManager packageManager;

  @Inject public ToAppMapper(PackageManager packageManager) {
    this.packageManager = packageManager;
  }

  @Override public App map(ApplicationInfo from) {
    return App.builder()
        .label(String.valueOf(packageManager.getApplicationLabel(from)))
        .packageName(from.packageName)
        .process(from.processName)
        .sourceDir(from.sourceDir)
        .flags(from.flags)
        .build();
  }
}
