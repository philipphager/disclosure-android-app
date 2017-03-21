package de.philipphager.disclosure.feature.sync.db.usecases;

import de.philipphager.disclosure.database.app.model.AppInfo;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.app.AppService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

/**
 * Find apps, that are saved in the database,
 * that are not present on the devices,
 * because they were uninstalled etc.
 */
public class FetchOutdatedPackages {
  private final DevicePackageProvider appProvider;
  private final AppService appService;
  private Observable<String> installedApps;

  @Inject public FetchOutdatedPackages(DevicePackageProvider appProvider,
      AppService appService) {
    this.appProvider = appProvider;
    this.appService = appService;
  }

  public Observable<List<String>> get() {
    loadInstalledApps();

    return appService.allInfos()
        .first()
        .flatMap(Observable::from)
        .flatMap(info -> Observable.just(info)
            .subscribeOn(Schedulers.computation())
            .map(AppInfo::packageName)
            .filter(packageName -> !appIsInstalled(packageName)))
        .distinct()
        .toList();
  }

  private void loadInstalledApps() {
    installedApps = appProvider.getInstalledPackages()
        .flatMap(Observable::from)
        .map(packageInfo -> packageInfo.packageName)
        .cache();
  }

  private boolean appIsInstalled(String packageName) {
    return ensureNotNull(installedApps, "Call loadInstalledApps() first!")
        .contains(packageName)
        .toBlocking()
        .first();
  }
}
