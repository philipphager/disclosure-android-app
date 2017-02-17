package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.feature.sync.db.usecases.FetchNewPermissions;
import de.philipphager.disclosure.feature.sync.db.usecases.FetchRequestedPermissions;
import de.philipphager.disclosure.service.PermissionService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class AnalyseUsedPermissions {
  private final PermissionService permissionService;
  private final FetchNewPermissions fetchNewPermissions;
  private final FetchRequestedPermissions fetchRequestedPermissions;

  @Inject public AnalyseUsedPermissions(PermissionService permissionService,
      FetchNewPermissions fetchNewPermissions,
      FetchRequestedPermissions fetchRequestedPermissions) {
    this.permissionService = permissionService;
    this.fetchNewPermissions = fetchNewPermissions;
    this.fetchRequestedPermissions = fetchRequestedPermissions;
  }

  public Observable<List<?>> analyse(App app) {
    Timber.d("analysing permissions used by %s", app);

    return Observable.concat(
        fetchNewPermissions.run(app)
            .doOnNext(permissionService::insertOrUpdate)
            .doOnError(Timber::e),
        fetchRequestedPermissions.run(app)
            .doOnNext(permissionService::insertForApp)
            .doOnError(Timber::e)
    ).ignoreElements();
  }
}
