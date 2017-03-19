package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.content.pm.PermissionInfo;
import android.os.Build;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.service.PermissionService;
import de.philipphager.disclosure.util.device.DeviceFeatures;
import de.philipphager.disclosure.util.ui.StringProvider;
import javax.inject.Inject;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class PermissionExplanationDialogPresenter {
  private final PermissionService permissionService;
  private final StringProvider stringProvider;
  private final DeviceFeatures deviceFeatures;
  private PermissionExplanationDialogView view;
  private App app;
  private Permission permission;
  private Boolean isGranted;
  private Boolean canBeRevoked;

  @Inject public PermissionExplanationDialogPresenter(PermissionService permissionService,
      StringProvider stringProvider,
      DeviceFeatures deviceFeatures) {
    this.permissionService = permissionService;
    this.stringProvider = stringProvider;
    this.deviceFeatures = deviceFeatures;
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
        view.showHint(stringProvider.getString(R.string.permission_status_normal));
      }

      if (PermissionInfo.PROTECTION_DANGEROUS == permission.protectionLevel()) {
        if (permissionCanBeRevoked()) {
          view.showHint(stringProvider.getString(R.string.permission_status_revokable));
        } else {
          view.showHint(stringProvider.getString(R.string.permission_status_no_runtime_permissions, Build.VERSION.RELEASE));
        }
      }
    } else {
      view.showHint(stringProvider.getString(R.string.permission_status_no_usage, app.label()));
    }
  }

  public void onRevokePermissionClicked() {
    view.navigate().toAppSystemSettings(app.packageName());
  }

  public boolean permissionCanBeRevoked() {
    if (canBeRevoked == null) {
      canBeRevoked = permission.protectionLevel() == PermissionInfo.PROTECTION_DANGEROUS
          && deviceFeatures.supportsRuntimePermissions()
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
