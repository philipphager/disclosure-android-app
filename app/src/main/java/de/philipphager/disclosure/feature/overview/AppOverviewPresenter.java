package de.philipphager.disclosure.feature.overview;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.service.AppService;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class AppOverviewPresenter {
  private final AppService appService;
  private AppOverviewView view;

  @Inject public AppOverviewPresenter(AppService appService) {
    this.appService = appService;
  }

  public void onCreate(AppOverviewView view) {
    this.view = view;
    loadApps();
  }

  private void loadApps() {
    view.showProgress();

    appService.userApps()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(apps -> {
          view.hideProgress();
          view.show(apps);
        }, throwable -> {
          view.hideProgress();
          Timber.e(throwable, "while loading all apps");
        });
  }

  public void onAppClicked(App app) {
    view.navigate().toDetail(app);
  }
}
