package de.philipphager.disclosure.feature.sync.usecases;

import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.model.AppModel;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class FetchOutdatedPackages {
  private final DevicePackageProvider appProvider;
  private final AppRepository appRepository;
  private Observable<String> installedApps;

  @Inject public FetchOutdatedPackages(DevicePackageProvider appProvider,
      AppRepository appRepository) {
    this.appProvider = appProvider;
    this.appRepository = appRepository;
  }

  public Observable<List<String>> get() {
    loadInstalledApps();

    return appRepository.allInfos()
        .first()
        .flatMap(Observable::from)
        .flatMap(info -> Observable.just(info)
            .subscribeOn(Schedulers.computation())
            .map(AppModel.SelectAllInfosModel::packageName)
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
