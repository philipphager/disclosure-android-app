package de.philipphager.disclosure.feature.analyser.permission.usecases;

import de.philipphager.disclosure.database.method.model.Method;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.service.MethodService;
import de.philipphager.disclosure.service.PermissionService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;

public class AnalysePermissionsFromMethodInvocations {
  private final PermissionService permissionService;
  private final MethodService methodService;

  @Inject public AnalysePermissionsFromMethodInvocations(PermissionService permissionService,
      MethodService methodService) {
    this.permissionService = permissionService;
    this.methodService = methodService;
  }

  public Observable<List<Permission>> run(List<Method> invokedMethods) {
    // TODO: load only permissions that are used by app.
    return permissionService.all()
        .first()
        .flatMap(Observable::from)
        .flatMap(permission -> methodService.byPermission(permission)
            .first()
            .subscribeOn(Schedulers.computation())
            .filter(protectedMethods -> findPermission(protectedMethods, invokedMethods))
            .map(methods -> permission)
        ).toList();
  }

  private boolean findPermission(List<Method> protectedMethods, List<Method> invokedMethods) {
    int i = 0;
    boolean permissionFound = false;

    while (i < protectedMethods.size() && !permissionFound) {
      permissionFound = invokedMethods.contains(protectedMethods.get(i));
      i++;
    }

    return permissionFound;
  }
}
