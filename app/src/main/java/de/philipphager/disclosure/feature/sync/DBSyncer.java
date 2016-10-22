package de.philipphager.disclosure.feature.sync;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.feature.sync.usecases.FetchOutdatedPackages;
import de.philipphager.disclosure.feature.sync.usecases.FetchUpdatedPackages;
import de.philipphager.disclosure.service.AppService;
import de.philipphager.disclosure.service.VersionService;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DBSyncer {
  private final Stopwatch stopwatch;
  private final FetchUpdatedPackages fetchUpdatedPackages;
  private final FetchOutdatedPackages fetchOutdatedApps;
  private final AppService appService;
  private final VersionService versionService;

  @Inject public DBSyncer(Stopwatch stopwatch,
      FetchUpdatedPackages fetchUpdatedPackages,
      FetchOutdatedPackages fetchOutdatedApps,
      AppService appService,
      VersionService versionService) {
    this.stopwatch = stopwatch;
    this.fetchUpdatedPackages = fetchUpdatedPackages;
    this.fetchOutdatedApps = fetchOutdatedApps;
    this.appService = appService;
    this.versionService = versionService;
  }

  public Observable<Integer> sync() {
    stopwatch.start();

    return Observable.concat(
        fetchUpdatedPackages.get().flatMap(this::updateAll).count(),
        fetchOutdatedApps.get().flatMap(this::deleteAll).count()
    ).doOnError(throwable -> {
      stopwatch.stop();
      Timber.e(throwable, "db sync failed, %s ", stopwatch);
    }).doOnCompleted(() -> {
      stopwatch.stop();
      Timber.d("db sync finished, %s ", stopwatch);
    });
  }

  private Observable<PackageInfo> updateAll(List<PackageInfo> packageInfos) {
    return Observable.from(packageInfos)
        .flatMap(i -> Observable.just(i)
            .subscribeOn(Schedulers.computation())
            .map(packageInfo -> {
              long appId = appService.add(packageInfo);
              versionService.add(appId, packageInfo);

              String thread = Thread.currentThread().getName();
              Timber.d("%s : inserting %s, version %s", thread, packageInfo.packageName,
                  packageInfo.versionCode);
              return packageInfo;
            })
        );
  }

  private Observable<String> deleteAll(List<String> packageNames) {
    return Observable.from(packageNames)
        .flatMap(p -> Observable.just(p)
            .subscribeOn(Schedulers.computation())
            .map(packageName -> {
              appService.removeByPackageName(packageName);

              String thread = Thread.currentThread().getName();
              Timber.d("%s : delete app %s", thread, packageName);
              return packageName;
            }));
  }
}
