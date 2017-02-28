package de.philipphager.disclosure.feature.sync.api;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.sync.api.usecases.SyncLibraries;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.util.List;
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

  public Observable<List<Library>> sync() {
    stopwatch.start();

    return syncLibraries.run()
        .doOnError(throwable -> {
          stopwatch.stop();
          Timber.e(throwable, "api sync failed, %s ", stopwatch);
        }).doOnCompleted(() -> {
          stopwatch.stop();
          Timber.d("api sync finished, %s ", stopwatch);
        }).ignoreElements();
  }
}
