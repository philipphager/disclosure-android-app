package de.philipphager.disclosure.feature.list;

import javax.inject.Inject;

public class AppListPresenter {
  private AppListView view;

  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public AppListPresenter() {
    // needed for dagger injection.
  }

  public void onCreate(AppListView view) {
    this.view = view;
  }
}
