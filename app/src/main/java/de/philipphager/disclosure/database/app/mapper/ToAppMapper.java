package de.philipphager.disclosure.database.app.mapper;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.Mapper;
import javax.inject.Inject;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class ToAppMapper implements Mapper<ApplicationInfo, App> {
  private final PackageManager packageManager;

  @Inject public ToAppMapper(PackageManager packageManager) {
    this.packageManager = packageManager;
  }

  @Override public App map(ApplicationInfo from) {
    CharSequence label = ensureNotNull(packageManager.getApplicationLabel(from),
        "could not load label for app");

    return App.builder()
        .label(label.toString())
        .packageName(from.packageName)
        .process(from.processName)
        .sourceDir(from.sourceDir)
        .flags(from.flags)
        .isTrusted(false)
        .build();
  }
}
