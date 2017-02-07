package de.philipphager.disclosure.feature.analyser.app.usecase;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.analyser.permission.ToPermissionMapper;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.PermissionService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.check;

public class AnalyseUsedPermissions {
  private final PermissionService permissionService;
  private final DevicePackageProvider packageProvider;
  private final ToPermissionMapper toPermissionMapper;

  @Inject public AnalyseUsedPermissions(PermissionService permissionService,
      DevicePackageProvider packageProvider,
      ToPermissionMapper toPermissionMapper) {
    this.permissionService = permissionService;
    this.packageProvider = packageProvider;
    this.toPermissionMapper = toPermissionMapper;
  }

  public Observable<List<?>> analyse(App app) {
    Timber.d("analysing permissions used by %s", app);

    return Observable.concat(
        loadRequestedPermissions(app)
            .doOnNext(permissionService::insertOrUpdate)
            .doOnError(Timber::e),
        loadPermissionStatus(app)
            .doOnNext(permissionService::insertForApp)
            .doOnError(Timber::e)
    ).ignoreElements();
  }

  private Observable<List<Permission>> loadRequestedPermissions(App app) {
    return packageProvider.getPackageInfo(app.packageName())
        .flatMap(packageInfo -> {
          String[] requestedPermission = packageInfo.requestedPermissions != null
              ? packageInfo.requestedPermissions : new String[0];
          return Observable.from(requestedPermission);
        })
        .flatMap(packageProvider::getPermissionInfo)
        .map(toPermissionMapper::map)
        .toList();
  }

  private Observable<List<AppPermission>> loadPermissionStatus(App app) {
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