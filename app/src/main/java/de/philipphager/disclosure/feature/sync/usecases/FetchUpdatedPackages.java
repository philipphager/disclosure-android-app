package de.philipphager.disclosure.feature.sync.usecases;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.ToInfoMapper;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class FetchUpdatedPackages {
  private final DevicePackageProvider appProvider;
  private final AppRepository appRepository;
  private final ToInfoMapper toInfoMapper;
  private Observable<App.Info> savedApps;

  @Inject public FetchUpdatedPackages(DevicePackageProvider appProvider,
      AppRepository appRepository,
      ToInfoMapper toInfoMapper) {
    this.appProvider = appProvider;
    this.appRepository = appRepository;
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

    savedApps = appRepository.allInfos()
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
