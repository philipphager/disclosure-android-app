package de.philipphager.disclosure.feature.sync.db.usecases;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.AppPermission;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.PermissionService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.check;
import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

/**
 * Returns all permissions, that were
 * requested by an app and if they
 * were granted by the system.
 */
public class FetchRequestedPermissions {
  private final DevicePackageProvider packageProvider;
  private final PermissionService permissionService;

  @Inject public FetchRequestedPermissions(DevicePackageProvider packageProvider,
      PermissionService permissionService) {
    this.packageProvider = packageProvider;
    this.permissionService = permissionService;
  }

  public Observable<List<AppPermission>> run(App app) {
    Observable<AppPermission> savedPermissions = loadSavedAppPermissions(app);

    return requestedAppPermissions(app)
        .flatMap(appPermissions -> Observable.from(appPermissions)
            .filter(appPermission -> !permissionIsSaved(savedPermissions, appPermission))
        ).toList()
        .doOnNext(appPermissions -> {
          Timber.d("", appPermissions);
        });
  }

  private Observable<List<AppPermission>> requestedAppPermissions(App app) {
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

  private Observable<AppPermission> loadSavedAppPermissions(App app) {
    Timber.d("operating on thread %s", Thread.currentThread().getName());

    return permissionService.byApp(app)
        .first()
        .flatMap(Observable::from)
        .cache();
  }

  private boolean permissionIsSaved(Observable<AppPermission> savedPermissions, AppPermission permission) {
    return ensureNotNull(savedPermissions, "call loadSavedPermissions() first!")
        .contains(permission)
        .toBlocking()
        .first();
  }
}
