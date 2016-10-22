package de.philipphager.disclosure.feature.list;

import de.philipphager.disclosure.feature.sync.DBSyncer;
import javax.inject.Inject;

public class AppListPresenter {
  private final DBSyncer dbSyncer;
  private AppListView view;

  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public AppListPresenter(DBSyncer dbSyncer) {
    // needed for dagger injection.
    this.dbSyncer = dbSyncer;
  }

  public void onCreate(AppListView view) {
    this.view = view;
    sayHello();
  }

  private void sayHello() {
    view.notify("Hello World");
    dbSyncer.sync()
        .subscribe(integer -> {

        }, throwable -> {

        });
  }
}
