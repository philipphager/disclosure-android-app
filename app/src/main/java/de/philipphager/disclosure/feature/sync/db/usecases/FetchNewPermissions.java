package de.philipphager.disclosure.feature.sync.db.usecases;

import android.content.pm.PermissionInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.analyser.permission.ToPermissionMapper;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.PermissionService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

/***
 * Fetch all permissions declared by an app, that are not already saved in the database. Because all
 * system permissions are saved in the db, this will normally return custom permissions, that are
 * defined by the apps themselves.
 */
public class FetchNewPermissions {
  private final DevicePackageProvider packageProvider;
  private final ToPermissionMapper toPermissionMapper;
  private final PermissionService permissionService;
  private Observable<String> savedPermissions;

  @Inject public FetchNewPermissions(DevicePackageProvider packageProvider,
      ToPermissionMapper toPermissionMapper,
      PermissionService permissionService) {
    this.packageProvider = packageProvider;
    this.toPermissionMapper = toPermissionMapper;
    this.permissionService = permissionService;
  }

  public Observable<List<Permission>> run(App app) {
    loadSavedPermissions();

    return fetchRequestedPermissions(app)
        .flatMap(permissions -> Observable.from(permissions)
            .filter(permission -> !permissionIsSaved(permission)))
        .toList();
  }

  public Observable<List<Permission>> fetchRequestedPermissions(App app) {
    return packageProvider.getPackageInfo(app.packageName())
        .flatMap(packageInfo -> {
          String[] requestedPermissions = packageInfo.requestedPermissions != null
              ? packageInfo.requestedPermissions : new String[0];
          return Observable.from(requestedPermissions);
        })
        .flatMap((id) -> packageProvider.getPermissionInfo(id)
            .map(permissionInfo -> {
              if (permissionInfo == null) {
                // Custom permissions might not have permission info,
                // but should still be saved in the database.
                permissionInfo = mapCustomPermission(id);
              }
              return permissionInfo;
            }))
        .map(toPermissionMapper::map)
        .toList();
  }

  private void loadSavedPermissions() {
    Timber.d("operating on thread %s", Thread.currentThread().getName());

    savedPermissions = permissionService.all()
        .first()
        .flatMap(Observable::from)
        .map(Permission::id)
        .cache();
  }

  private boolean permissionIsSaved(Permission permission) {
    return ensureNotNull(savedPermissions, "call loadSavedPermissions() first!")
        .contains(permission.id())
        .toBlocking()
        .first();
  }

  private PermissionInfo mapCustomPermission(String id) {
    PermissionInfo permissionInfo = new PermissionInfo();
    permissionInfo.name = id;
    return permissionInfo;
  }
}
