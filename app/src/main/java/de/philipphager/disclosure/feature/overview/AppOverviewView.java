package de.philipphager.disclosure.feature.overview;

import de.philipphager.disclosure.database.app.model.App;
import java.util.List;

public interface AppOverviewView {
  void notify(String message);

  void show(List<App> apps);
}
