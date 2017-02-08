package de.philipphager.disclosure.feature.app.detail;

import android.content.Intent;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.util.ui.components.ScoreView;
import java.util.List;

public interface DetailView extends Navigates {
  void notify(String message);

  void setToolbarTitle(String title);

  void setAppIcon(String packageName);

  void setLibraries(List<Library> libraries);

  void setScore(ScoreView.Score score);

  void showEditPermissionsTutorial(String packageName);

  void showRuntimePermissionsTutorial(String packageName);

  void enableEditPermissions(boolean isEnabled);

  void setAppIsTrusted(boolean isTrusted);

  void startActivityForResult(Intent intent, int requestCode);

  void finish();
}
