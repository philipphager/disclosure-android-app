package de.philipphager.disclosure.feature.app.detail.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.service.LibraryService;
import de.philipphager.disclosure.service.PermissionService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class FetchLibrariesForAppWithPermissions {
  private final LibraryService libraryService;
  private final PermissionService permissionService;
  private Observable<Permission> grantedPermissions;

  @Inject public FetchLibrariesForAppWithPermissions(LibraryService libraryService,
      PermissionService permissionService) {
    this.libraryService = libraryService;
    this.permissionService = permissionService;
  }

  public Observable<List<LibraryWithPermission>> run(App app, boolean showAll) {
    loadGrantedPermissionsForApp(app);

    return libraryService.byAppUpdateOnPermissionChange(app)
        .flatMap(libraries -> Observable.from(libraries)
            .distinct()
            .flatMap(library -> permissionService.byAppAndLibrary(app, library)
                .first()
                .flatMap(Observable::from)
                .filter(permission -> showAll || permissionIsGrantedForApp(permission))
                .toList()
                .map(permissions -> LibraryWithPermission.create(library, permissions)))
            .toSortedList((library, otherLibrary) -> {
              return otherLibrary.permissions().size() - library.permissions().size();
            }));
  }

  private void loadGrantedPermissionsForApp(App app) {
    Timber.d("operating on thread %s", Thread.currentThread().getName());

    grantedPermissions = permissionService.byApp(app, true)
        .first()
        .flatMap(Observable::from)
        .cache();
  }

  private boolean permissionIsGrantedForApp(Permission permission) {
    return ensureNotNull(grantedPermissions, "call loadInstalledApps() first!")
        .contains(permission)
        .toBlocking()
        .first();
  }
}
