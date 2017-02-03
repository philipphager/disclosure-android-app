package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.library.usecase.AnalyseUsedLibraries;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AnalyseApps {
  private final AnalyseUsedLibraries analyseUsedLibraries;
  private final AnalyseUsedPermissions analyseUsedPermissions;
  private final Stopwatch stopwatch;

  @Inject public AnalyseApps(AnalyseUsedLibraries analyseUsedLibraries,
      AnalyseUsedPermissions analyseUsedPermissions,
      Stopwatch stopwatch) {
    this.analyseUsedLibraries = analyseUsedLibraries;
    this.analyseUsedPermissions = analyseUsedPermissions;
    this.stopwatch = stopwatch;
  }

  public Observable<?> analyse(List<App> apps) {
    stopwatch.start();

    return Observable.from(apps)
        .flatMap(app -> Observable.just(app)
            .subscribeOn(Schedulers.computation())
            .flatMap(app1 -> Observable.concat(
                analyseUsedLibraries.analyse(app1),
                analyseUsedPermissions.analyse(app1)
            ))
            .doOnTerminate(() -> Timber.d("finished analysing %s", app))
            .onErrorResumeNext(Observable.empty())
        ).doOnTerminate(() -> {
          stopwatch.stop();
          Timber.d("finished app analysis: %s", stopwatch);
        }).ignoreElements();
  }
}
