package de.philipphager.disclosure.feature.analyser;

import android.support.annotation.Nullable;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.app.usecase.AnalyseAppLibraryPermission;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

@Singleton public class AppAnalyticsService {
  private final AnalyseAppLibraryPermission analyseAppLibraryPermission;
  private final Queue<App> apps;
  private final BehaviorSubject<List<App>> pendingApps;
  private Subscription currentAnalysis;

  @Inject public AppAnalyticsService(AnalyseAppLibraryPermission analyseAppLibraryPermission) {
    this.analyseAppLibraryPermission = analyseAppLibraryPermission;
    this.pendingApps = BehaviorSubject.create();
    this.apps = new LinkedList<>();
  }

  public void enqueue(App app) {
    this.apps.add(app);
    onQueueChanged();
  }

  public void remove(App app) {
    ensureNotNull(app, "must provide app to remove analysis");

    if (app.equals(getCurrent())) {
      cancelAnalysis(app);
    } else {
      dequeue(app);
    }
  }

  public boolean contains(App app) {
    return apps.contains(app);
  }

  @Nullable public App getCurrent() {
    return this.apps.peek();
  }

  @Nullable public Observable<List<App>> getPendingApps() {
    return pendingApps.serialize().onBackpressureBuffer();
  }

  public void removePendingApps() {
    List<App> pendingApps = filterPendingApps();

    for (App app : pendingApps) {
      dequeue(app);
    }
  }

  public boolean isAnalyzing() {
    return currentAnalysis != null;
  }

  public Observable<AnalyticsProgress> getProgress() {
    return analyseAppLibraryPermission.getProgress()
        .map(state -> {
          if (getCurrent() != null) {
            return AnalyticsProgress.create(getCurrent(), state);
          } else {
            return null;
          }
        }).filter(progress -> progress != null);
  }

  private void startAnalysis(App app) {
    currentAnalysis = analyseAppLibraryPermission.run(app)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate(() -> endAnalysis(app))
        .subscribe(permissions -> {

        }, throwable -> {
          Timber.e(throwable, "while analyzing app");
        });
  }

  private void onQueueChanged() {
    this.pendingApps.onNext(filterPendingApps());

    // Start analysis for first item in queue.
    if (currentAnalysis == null && this.apps.peek() != null) {
      this.startAnalysis(this.apps.peek());
    }
  }

  private List<App> filterPendingApps() {
    List<App> pendingApps = new ArrayList<>(this.apps);

    // Has pending apps
    if (!pendingApps.isEmpty()) {
      pendingApps.remove(0);
    }
    return pendingApps;
  }

  private void endAnalysis(App app) {
    currentAnalysis.unsubscribe();
    currentAnalysis = null;
    dequeue(app);
  }

  private void cancelAnalysis(App app) {
    analyseAppLibraryPermission.cancel();
    endAnalysis(app);
  }

  private void dequeue(App app) {
    this.apps.remove(app);
    this.onQueueChanged();
  }
}
