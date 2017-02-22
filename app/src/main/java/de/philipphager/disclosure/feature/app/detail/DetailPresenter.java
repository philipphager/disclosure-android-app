package de.philipphager.disclosure.feature.app.detail;

import android.content.Intent;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.analyser.app.usecase.AnalyseAppLibraryPermissions;
import de.philipphager.disclosure.feature.app.detail.usecase.FetchLibrariesForAppWithPermissions;
import de.philipphager.disclosure.feature.preference.ui.DisplayAllPermissions;
import de.philipphager.disclosure.feature.preference.ui.HasSeenEditPermissionsTutorial;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.util.device.IntentFactory;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;
import static de.philipphager.disclosure.util.device.DeviceFeatures.supportsRuntimePermissions;

public class DetailPresenter {
  private static final int UNINSTALL_REQUEST_CODE = 39857;

  private final AppService appService;
  private final IntentFactory intentFactory;
  private final AnalyseAppLibraryPermissions analyseAppLibraryPermissions;
  private final Preference<Boolean> hasSeenEditPermissionsTutorial;
  private final Preference<Boolean> displayAllPermissions;
  private final FetchLibrariesForAppWithPermissions fetchLibrariesForAppWithPermissions;
  private CompositeSubscription subscriptions;
  private Subscription analyticsSubscription;
  private DetailView view;
  private App app;

  @Inject public DetailPresenter(AppService appService,
      IntentFactory intentFactory,
      AnalyseAppLibraryPermissions analyseAppLibraryPermissions,
      @HasSeenEditPermissionsTutorial Preference<Boolean> hasSeenEditPermissionsTutorial,
      @DisplayAllPermissions Preference<Boolean> displayAllPermissions,
      FetchLibrariesForAppWithPermissions fetchLibrariesForAppWithPermissions) {
    this.appService = appService;
    this.intentFactory = intentFactory;
    this.analyseAppLibraryPermissions = analyseAppLibraryPermissions;
    this.hasSeenEditPermissionsTutorial = hasSeenEditPermissionsTutorial;
    this.displayAllPermissions = displayAllPermissions;
    this.fetchLibrariesForAppWithPermissions = fetchLibrariesForAppWithPermissions;
  }

  public void onCreate(DetailView view, App app) {
    this.view = ensureNotNull(view);
    this.app = ensureNotNull(app);
    this.subscriptions = new CompositeSubscription();

    fetchAppUpdates();
    fetchLibraries();
    fetchAnalysisUpdates();
  }

  public void onDestroy() {
    this.subscriptions.clear();
  }

  private void initView(App app) {
    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());
    view.enableEditPermissions(supportsRuntimePermissions());
    view.setAppIsTrusted(app.isTrusted());
  }

  private void fetchAppUpdates() {
    subscriptions.add(appService.byPackageName(app.packageName())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(app -> {
          this.app = app;
          initView(app);
        }));
  }

  private void fetchLibraries() {
    subscriptions.add(fetchLibrariesForAppWithPermissions.run(app, displayAllPermissions.get())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraryWithPermissions -> {
          view.setLibraries(libraryWithPermissions);
        }, throwable -> Timber.e(throwable, "while observing analysis progress")));
  }

  private void fetchAnalysisUpdates() {
    subscriptions.add(analyseAppLibraryPermissions.getProgress()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(state -> view.setAnalysisProgress(state), Timber::e));
  }

  public void onLibraryClicked(Library library) {
    view.navigate().toLibraryDetail(library);
  }

  public void onAnalyseAppClicked() {
    view.showAnalysisProgress();
    view.resetProgress();

    if (analyticsSubscription == null) {
      analyticsSubscription = analyseAppLibraryPermissions.run(app)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnTerminate(() -> {
            view.setAnalysisCompleted();
            view.hideAnalysisProgress();
            analyticsSubscription = null;
          })
          .subscribe(permissions -> {
            view.notify(String.format("found %s", permissions));
          }, Timber::e);

      subscriptions.add(analyticsSubscription);
    } else {
      view.showCancel();
    }
  }

  public void cancelAnalyseApp() {
    if (analyticsSubscription != null) {
      analyticsSubscription.unsubscribe();
      analyticsSubscription = null;
      view.hideAnalysisProgress();
    }
  }

  public void onEditPermissionsClicked() {
    if (supportsRuntimePermissions()) {
      editPermissions();
    } else {
      view.showRuntimePermissionsTutorial(app.packageName());
    }
  }

  public void onTrustAppClicked() {
    App tempApp = this.app.editTrust(this.app, !app.isTrusted());
    appService.insertOrUpdate(tempApp);
    app = tempApp;
  }

  public boolean onEditPermissionsLongClicked() {
    view.showEditPermissionsTutorial(app.packageName());
    return true;
  }

  public void onUninstallClicked() {
    Intent uninstallIntent = intentFactory.uninstallPackage(app.packageName());
    view.startActivityForResult(uninstallIntent, UNINSTALL_REQUEST_CODE);
  }

  public void onActivityResult(int requestCode, int resultCode) {
    if (requestCode == UNINSTALL_REQUEST_CODE && resultCode == RESULT_OK) {
      onUninstallSucceeded();
    }
  }

  public void onUninstallSucceeded() {
    view.finish();
  }

  private void editPermissions() {
    if (!hasSeenEditPermissionsTutorial.get()) {
      view.showEditPermissionsTutorial(app.packageName());
    } else {
      view.navigate().toAppSystemSettings(app.packageName());
    }
  }
}
