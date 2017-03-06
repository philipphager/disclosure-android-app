package de.philipphager.disclosure.feature.app.detail;

import android.content.Intent;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.app.detail.usecase.LibraryWithPermission;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.util.ui.components.ProtectionLevelView;
import java.util.List;

public interface DetailView extends Navigates, AnalysisProgressView {
  void notify(String message);

  void notifyAnalysisResult(String appLabel, int permissionCount, int libraryCount);

  void setToolbarTitle(String title);

  void setAppIcon(String packageName);

  void setLibraries(List<LibraryWithPermission> libraries);

  void showEditPermissionsTutorial(String packageName);

  void showRuntimePermissionsTutorial(String packageName);

  void showPermissionExplanation(App app, Permission permission);

  void enableEditPermissions(boolean isEnabled);

  void startActivityForResult(Intent intent, int requestCode);

  void finish();
}
