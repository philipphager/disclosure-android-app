package de.philipphager.disclosure.feature.app.overview;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.analyser.app.usecases.AnalyseApps;
import de.philipphager.disclosure.service.AppService;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AppOverviewPresenter {
  private final AppService appService;
  private final AnalyseApps analyseApps;
  private CompositeSubscription subscriptions;
  private AppOverviewView view;

  @Inject public AppOverviewPresenter(AppService appService, AnalyseApps analyseApps) {
    this.appService = appService;
    this.analyseApps = analyseApps;
  }

  public void onCreate(AppOverviewView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();
    loadApps();
  }

  public void onDestory() {
    subscriptions.unsubscribe();
  }

  private void loadApps() {
    view.showProgress();

    subscriptions.add(appService.userApps()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(apps -> {
          view.hideProgress();
          view.show(apps);
        }, throwable -> {
          view.hideProgress();
          Timber.e(throwable, "while loading all apps");
        }));
  }

  public void onAnalyseAllClicked() {
    view.showProgress();

    subscriptions.add(
        appService.userApps()
            .first()
            .flatMap(analyseApps::analyse)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(() -> view.hideProgress())
            .subscribe(libraries -> {},
                throwable -> Timber.e(throwable, "while analysing all apps"),
                () -> view.notify("all apps analysed!")
            ));
  }

  public void onAppClicked(App app) {
    view.navigate().toAppDetail(app);
  }
}
