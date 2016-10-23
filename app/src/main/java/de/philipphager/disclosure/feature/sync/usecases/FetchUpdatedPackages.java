package de.philipphager.disclosure.feature.sync.usecases;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.info.mapper.ToInfoMapper;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.AppService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class FetchUpdatedPackages {
  private final DevicePackageProvider appProvider;
  private final AppService appService;
  private final ToInfoMapper toInfoMapper;
  private Observable<App.Info> savedApps;

  @Inject public FetchUpdatedPackages(DevicePackageProvider appProvider,
      AppService appService,
      ToInfoMapper toInfoMapper) {
    this.appProvider = appProvider;
    this.appService = appService;
    this.toInfoMapper = toInfoMapper;
  }

  public Observable<List<PackageInfo>> get() {
    loadSavedApps();

    return appProvider.getInstalledPackages()
        .flatMap(Observable::from)
        .flatMap(packageInfo -> Observable.just(packageInfo)
            .subscribeOn(Schedulers.computation())
            .map(toInfoMapper::map)
            .filter(app -> !appIsSaved(app))
            .map(info -> packageInfo))
        .toList();
  }

  private void loadSavedApps() {
    Timber.d("operating on thread %s", Thread.currentThread().getName());

    savedApps = appService.allAppInfos()
        .first()
        .flatMap(Observable::from)
        .cache();
  }

  private boolean appIsSaved(App.Info app) {
    return ensureNotNull(savedApps, "call loadInstalledApps() first!")
        .contains(app)
        .toBlocking()
        .first();
  }
}
