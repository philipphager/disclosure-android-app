package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.content.pm.PermissionInfo;
import de.philipphager.disclosure.database.permission.model.Permission;
import javax.inject.Inject;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;
import static de.philipphager.disclosure.util.device.DeviceFeatures.supportsRuntimePermissions;

public class PermissionExplanationDialogPresenter {
  private PermissionExplanationDialogView view;
  private String packageName;
  private Permission permission;

  @Inject public PermissionExplanationDialogPresenter() {
  }

  public void onCreate(PermissionExplanationDialogView view, String packageName,
      Permission permission) {
    this.view = ensureNotNull(view, "must provide view.");
    this.packageName = ensureNotNull(packageName, "must provide app packageName");
    this.permission = ensureNotNull(permission, "must provide permission");
  }

  public boolean permissionCanBeRevoked() {
    return permission.protectionLevel() == PermissionInfo.PROTECTION_DANGEROUS
        && supportsRuntimePermissions();
  }

  public void onRevokePermissionClicked() {
    view.navigate().toAppSystemSettings(packageName);
  }
}
