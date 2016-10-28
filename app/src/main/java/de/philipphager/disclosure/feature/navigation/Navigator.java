package de.philipphager.disclosure.feature.navigation;

import android.app.Activity;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.detail.DetailActivity;
import de.philipphager.disclosure.feature.home.HomeActivity;
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

  public void toDetail(App app) {
    activity.startActivity(DetailActivity.launch(activity, app));
  }
}
