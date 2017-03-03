package de.philipphager.disclosure.feature.app.detail.tutorials;

import de.philipphager.disclosure.feature.navigation.Navigates;

public interface PermissionExplanationDialogView extends Navigates {
  void showHint(String text);

  void showPermissionStatus(boolean isGranted, boolean canBeRevoked);
}
