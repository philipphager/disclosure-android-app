package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.content.pm.PermissionInfo;
import android.os.Build;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.service.PermissionService;
import javax.inject.Inject;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;
import static de.philipphager.disclosure.util.device.DeviceFeatures.supportsRuntimePermissions;

public class PermissionExplanationDialogPresenter {
  private final PermissionService permissionService;
  private PermissionExplanationDialogView view;
  private App app;
  private Permission permission;
  private Boolean isGranted;
  private Boolean canBeRevoked;

  @Inject public PermissionExplanationDialogPresenter(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  public void onCreate(PermissionExplanationDialogView view, App app,
      Permission permission) {
    this.view = ensureNotNull(view, "must provide view.");
    this.app = ensureNotNull(app, "must provide app app");
    this.permission = ensureNotNull(permission, "must provide permission");

    initUi();
  }

  private void initUi() {
    view.showPermissionStatus(permissionWasGrantedForApp(), permissionCanBeRevoked());

    if (permissionWasGrantedForApp()) {


      if (PermissionInfo.PROTECTION_NORMAL == permission.protectionLevel()) {
        view.showHint("This permission can be actively used by the library, but you cannot revoked it, since normal permissions are automatically granted by the system.");
      }

      if (PermissionInfo.PROTECTION_DANGEROUS == permission.protectionLevel()) {
        if (permissionCanBeRevoked()) {
          view.showHint("This permission can be actively used by the library and can be revoked. Click on 'edit settins' to revoke this permission in the system settings");
        } else {
          view.showHint("This permission can be actively used by the library, but your device runs " + Build.VERSION.RELEASE + " and does not support revoking permissions. You can only uninstall this app.");
        }
      }
    } else {
      view.showHint("This permisssion usage was detected in the library's source code, but is not actively used, because " + app.label() + " does not have the permission.");
    }
  }

  public void onRevokePermissionClicked() {
    view.navigate().toAppSystemSettings(app.packageName());
  }

  public boolean permissionCanBeRevoked() {
    if (canBeRevoked == null) {
      canBeRevoked = permission.protectionLevel() == PermissionInfo.PROTECTION_DANGEROUS
          && supportsRuntimePermissions()
          && permissionWasGrantedForApp();
    }

    return canBeRevoked;
  }

  public boolean permissionWasGrantedForApp() {
    if (isGranted == null) {
      isGranted = permissionService.byApp(app, true)
          .toBlocking()
          .first()
          .contains(permission);
    }

    return isGranted;
  }
}
