package de.philipphager.disclosure.feature.sync.db.usecases;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

import static de.philipphager.disclosure.util.assertion.Assertions.check;

public class FetchRequestedPermissions {
  private final DevicePackageProvider packageProvider;

  @Inject public FetchRequestedPermissions(DevicePackageProvider packageProvider) {
    this.packageProvider = packageProvider;
  }

  public Observable<List<AppPermission>> run(App app) {
    return packageProvider.getPackageInfo(app.packageName())
        .map(packageInfo -> {
          String[] permissions = packageInfo.requestedPermissions != null
              ? packageInfo.requestedPermissions : new String[0];
          int[] flags = packageInfo.requestedPermissionsFlags != null
              ? packageInfo.requestedPermissionsFlags : new int[0];

          check(permissions.length == flags.length, "not every permission has a flag assigned");

          List<AppPermission> appPermissions = new ArrayList<>(permissions.length);
          for (int i = 0; i < permissions.length; i++) {
            boolean isGranted = (flags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0;
            appPermissions.add(AppPermission.create(app.id(), permissions[i], isGranted));
          }
          return appPermissions;
        });
  }
}
