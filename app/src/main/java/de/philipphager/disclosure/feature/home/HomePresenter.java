package de.philipphager.disclosure.feature.home;

import de.philipphager.disclosure.feature.sync.DBSyncer;
import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class HomePresenter {
  private final DBSyncer dbSyncer;
  private CompositeSubscription subscriptions;
  private HomeView homeView;

  @Inject public HomePresenter(DBSyncer dbSyncer) {
    this.dbSyncer = dbSyncer;
  }

  public void onCreate(HomeView homeView) {
    this.homeView = homeView;
    this.subscriptions = new CompositeSubscription();

    syncDBWithDevice();
  }

  public void onDestroy() {
    this.subscriptions.unsubscribe();
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
