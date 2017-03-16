package de.philipphager.disclosure.feature.analyser.permission;

import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.util.Mapper;
import javax.inject.Inject;

public class ToPermissionMapper implements Mapper<PermissionInfo, Permission> {
  private final PackageManager packageManager;

  @Inject public ToPermissionMapper(PackageManager packageManager) {
    // Needed for dagger injection;
    this.packageManager = packageManager;
  }

  @Override public Permission map(PermissionInfo permissionInfo) {
    CharSequence name = permissionInfo.loadLabel(packageManager);
    CharSequence description = permissionInfo.loadDescription(packageManager);

    return Permission.builder()
        .id(permissionInfo.name)
        .title(name != null ? name.toString() : "")
        .description(description != null ? description.toString() : "")
        .permissionGroup(permissionInfo.group)
        .protectionLevel(permissionInfo.protectionLevel)
        .build();
  }
}
