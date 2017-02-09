package de.philipphager.disclosure.feature.app.detail;

import android.content.Intent;
import de.philipphager.disclosure.feature.app.detail.usecase.LibraryWithPermission;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.util.ui.components.ScoreView;
import java.util.List;

public interface DetailView extends Navigates, AnalysisProgressView {
  void notify(String message);

  void setToolbarTitle(String title);

  void setAppIcon(String packageName);

  void setLibraries(List<LibraryWithPermission> libraries);

  void setScore(ScoreView.Score score);

  void showEditPermissionsTutorial(String packageName);

  void showRuntimePermissionsTutorial(String packageName);

  void enableEditPermissions(boolean isEnabled);

  void setAppIsTrusted(boolean isTrusted);

  void startActivityForResult(Intent intent, int requestCode);

  void finish();
}
