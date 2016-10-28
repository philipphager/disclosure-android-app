package de.philipphager.disclosure.feature.detail;

import de.philipphager.disclosure.database.app.model.App;
import javax.inject.Inject;

public class DetailPresenter {
  private DetailView view;
  private App app;

  @Inject public DetailPresenter() {
  }

  public void onCreate(DetailView view, App app) {
    this.view = view;
    this.app = app;

    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());
  }
}
