package de.philipphager.disclosure.feature.sync.api;

import de.philipphager.disclosure.feature.sync.api.usecases.SyncLibraries;
import de.philipphager.disclosure.util.time.Stopwatch;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class ApiSyncer {
  private final SyncLibraries syncLibraries;
  private final Stopwatch stopwatch;

  @Inject public ApiSyncer(SyncLibraries syncLibraries,
      Stopwatch stopwatch) {
    this.syncLibraries = syncLibraries;
    this.stopwatch = stopwatch;
  }

  public Observable<Void> sync() {
    stopwatch.start();

    return syncLibraries.run()
        .doOnError(throwable -> {
          stopwatch.stop();
          Timber.e(throwable, "api sync failed, %s ", stopwatch);
        }).doOnCompleted(() -> {
          stopwatch.stop();
          Timber.d("api sync finished, %s ", stopwatch);
        })
        .map(ignored -> null);
  }
}
