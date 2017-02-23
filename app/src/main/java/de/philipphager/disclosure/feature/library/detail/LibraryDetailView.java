package de.philipphager.disclosure.feature.library.detail;

import de.philipphager.disclosure.database.app.model.AppWithPermissions;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface LibraryDetailView extends Navigates {
  void showApps(List<AppWithPermissions> apps);
}
