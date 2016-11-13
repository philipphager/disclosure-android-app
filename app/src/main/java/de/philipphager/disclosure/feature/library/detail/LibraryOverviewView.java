package de.philipphager.disclosure.feature.library.detail;

import de.philipphager.disclosure.database.app.model.App;
import java.util.List;

public interface LibraryOverviewView {
  void showApps(List<App> apps);
}
