package de.philipphager.disclosure.feature.home;

import de.philipphager.disclosure.feature.sync.DBSyncer;
import javax.inject.Inject;
import timber.log.Timber;

public class HomePresenter {
  private final DBSyncer dbSyncer;

  @Inject public HomePresenter(DBSyncer dbSyncer) {
    this.dbSyncer = dbSyncer;
  }

  public void onCreate() {
    dbSyncer.sync()
        .subscribe(i -> {

        }, throwable -> {
          Timber.e(throwable, "while syncing apps with the device.");
        }, () -> {
          Timber.d("database sync is completed.");
        });
  }
}
