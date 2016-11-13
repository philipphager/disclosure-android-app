package de.philipphager.disclosure.feature.sync.api;

import de.philipphager.disclosure.feature.sync.api.usecases.SyncFeatures;
import de.philipphager.disclosure.feature.sync.api.usecases.SyncLibraries;
import de.philipphager.disclosure.feature.sync.api.usecases.SyncLibraryFeatures;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class ApiSyncer {
  private final SyncLibraries syncLibraries;
  private final SyncFeatures syncFeatures;
  private final SyncLibraryFeatures syncLibraryFeatures;
  private final Stopwatch stopwatch;

  @Inject public ApiSyncer(SyncLibraries syncLibraries,
      SyncFeatures syncFeatures,
      SyncLibraryFeatures syncLibraryFeatures,
      Stopwatch stopwatch) {
    this.syncLibraries = syncLibraries;
    this.syncFeatures = syncFeatures;
    this.syncLibraryFeatures = syncLibraryFeatures;
    this.stopwatch = stopwatch;
  }

  public Observable<List<?>> sync() {
    stopwatch.start();

    return Observable.concat(
        Observable.concatEager(syncLibraries.run(), syncFeatures.run()),
        syncLibraryFeatures.run()
    ).doOnError(throwable -> {
      stopwatch.stop();
      Timber.e(throwable, "api sync failed, %s ", stopwatch);
    }).doOnCompleted(() -> {
      stopwatch.stop();
      Timber.d("api sync finished, %s ", stopwatch);
    }).ignoreElements();
  }
}
