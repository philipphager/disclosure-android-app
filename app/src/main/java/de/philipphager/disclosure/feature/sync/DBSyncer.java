package de.philipphager.disclosure.feature.sync;

import de.philipphager.disclosure.feature.sync.usecases.FetchOutdatedPackages;
import de.philipphager.disclosure.feature.sync.usecases.FetchUpdatedPackages;
import de.philipphager.disclosure.service.AppService;
import de.philipphager.disclosure.util.time.Stopwatch;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class DBSyncer {
  private final Stopwatch stopwatch;
  private final FetchUpdatedPackages fetchUpdatedPackages;
  private final FetchOutdatedPackages fetchOutdatedApps;
  private final AppService appService;

  @Inject public DBSyncer(Stopwatch stopwatch,
      FetchUpdatedPackages fetchUpdatedPackages,
      FetchOutdatedPackages fetchOutdatedApps,
      AppService appService) {
    this.stopwatch = stopwatch;
    this.fetchUpdatedPackages = fetchUpdatedPackages;
    this.fetchOutdatedApps = fetchOutdatedApps;
    this.appService = appService;
  }

  public Observable<Integer> sync() {
    stopwatch.start();

    return Observable.concat(
        fetchUpdatedPackages.get().doOnNext(appService::addAll).count(),
        fetchOutdatedApps.get().doOnNext(appService::removeAllByPackageName).count()
    ).doOnError(throwable -> {
      stopwatch.stop();
      Timber.e(throwable, "db sync failed, %s ", stopwatch);
    }).doOnCompleted(() -> {
      stopwatch.stop();
      Timber.d("db sync finished, %s ", stopwatch);
    });
  }
}
