package de.philipphager.disclosure.feature.navigation;

import android.app.Activity;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.app.detail.DetailActivity;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.library.detail.LibraryOverviewActivity;
import javax.inject.Inject;

public class Navigator {
  private Activity activity;

  @SuppressWarnings("PMD.UnnecessaryConstructor") @Inject public Navigator() {
    // Needed for dagger injection.
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public void toHome() {
    activity.startActivity(HomeActivity.launch(activity));
  }

  public void toAppDetail(App app) {
    activity.startActivity(DetailActivity.launch(activity, app));
  }

  public void toLibraryOverview(Library library) {
    activity.startActivity(LibraryOverviewActivity.launch(activity, library));
  }
}
