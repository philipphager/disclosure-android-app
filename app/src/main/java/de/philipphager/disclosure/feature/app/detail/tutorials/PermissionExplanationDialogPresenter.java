package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.content.pm.PermissionInfo;
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

  @Inject public PermissionExplanationDialogPresenter(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  public void onCreate(PermissionExplanationDialogView view, App app,
      Permission permission) {
    this.view = ensureNotNull(view, "must provide view.");
    this.app = ensureNotNull(app, "must provide app app");
    this.permission = ensureNotNull(permission, "must provide permission");
  }

  public boolean permissionCanBeRevoked() {
    return permission.protectionLevel() == PermissionInfo.PROTECTION_DANGEROUS
        && supportsRuntimePermissions()
        && permissionService.byApp(app, true)
        .toBlocking()
        .first()
        .contains(permission);
  }

  public void onRevokePermissionClicked() {
    view.navigate().toAppSystemSettings(app.packageName());
  }
}
