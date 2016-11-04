package de.philipphager.disclosure.feature.analyser.library.usecases;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AnalyseApps {
  private final AnalyseUsedLibraries analyseUsedLibraries;
  private final Stopwatch stopwatch;

  @Inject public AnalyseApps(AnalyseUsedLibraries analyseUsedLibraries, Stopwatch stopwatch) {
    this.analyseUsedLibraries = analyseUsedLibraries;
    this.stopwatch = stopwatch;
  }

  public Observable<List<Library>> analyse(List<App> apps) {
    stopwatch.start();

    return Observable.from(apps)
        .flatMap(app -> Observable.just(app)
            .subscribeOn(Schedulers.computation())
            .flatMap(analyseUsedLibraries::analyse)
            .doOnTerminate(() -> Timber.d("finished analysing %s", app))
            .onErrorResumeNext(Observable.empty())
        ).doOnTerminate(() -> {
          stopwatch.stop();
          Timber.d("finished app analysis: %s", stopwatch);
        })
        .ignoreElements();
  }
}
