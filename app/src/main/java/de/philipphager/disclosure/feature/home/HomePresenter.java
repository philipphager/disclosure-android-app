package de.philipphager.disclosure.feature.home;

import de.philipphager.disclosure.feature.sync.api.ApiSyncer;
import de.philipphager.disclosure.feature.sync.db.DBSyncer;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class HomePresenter {
  private final DBSyncer dbSyncer;
  private final ApiSyncer apiSyncer;
  private CompositeSubscription subscriptions;
  private HomeView homeView;

  @Inject public HomePresenter(DBSyncer dbSyncer, ApiSyncer apiSyncer) {
    this.dbSyncer = dbSyncer;
    this.apiSyncer = apiSyncer;
  }

  public void onCreate(HomeView homeView) {
    this.homeView = homeView;
    this.subscriptions = new CompositeSubscription();

    syncWithApi();
    syncDBWithDevice();
  }

  public void onDestroy() {
    this.subscriptions.clear();
  }

  private void syncWithApi() {
    subscriptions.add(apiSyncer.sync()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraries -> {}, Timber::e));
  }

  private void syncDBWithDevice() {
    subscriptions.add(dbSyncer.sync()
        .subscribe(i -> {

        }, throwable -> {
          Timber.e(throwable, "while syncing apps with the device.");
        }, () -> {
          Timber.d("database sync completed.");
        }));
  }

  public boolean onTabSelected(int position, boolean wasSelected) {
    if (!wasSelected) {
      homeView.setCurrentTab(position);
    }
    return true;
  }
}
