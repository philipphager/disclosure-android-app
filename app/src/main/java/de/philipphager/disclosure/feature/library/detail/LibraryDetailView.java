package de.philipphager.disclosure.feature.library.detail;

import de.philipphager.disclosure.database.app.model.App;
import java.util.List;

public interface LibraryDetailView {
  void showApps(List<App> apps);
}
