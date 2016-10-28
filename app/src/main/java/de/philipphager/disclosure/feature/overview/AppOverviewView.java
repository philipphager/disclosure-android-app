package de.philipphager.disclosure.feature.overview;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface AppOverviewView extends Navigates {
  void notify(String message);

  void show(List<App> apps);

  void showProgress();

  void hideProgress();
}
