package de.philipphager.disclosure.feature.app.detail;

import android.content.Intent;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.analyser.AppAnalyticsService;
import de.philipphager.disclosure.feature.analyser.app.usecase.AnalyseUsedPermissions;
import de.philipphager.disclosure.feature.app.detail.usecase.FetchLibrariesForAppWithPermissions;
import de.philipphager.disclosure.feature.preference.ui.DisplayAllPermissions;
import de.philipphager.disclosure.feature.preference.ui.HasSeenEditPermissionsTutorial;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.util.device.DeviceFeatures;
import de.philipphager.disclosure.util.device.IntentFactory;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class AppDetailPresenter {
  private static final int UNINSTALL_REQUEST_CODE = 39857;

  private final AppService appService;
  private final IntentFactory intentFactory;
  private final Preference<Boolean> hasSeenEditPermissionsTutorial;
  private final Preference<Boolean> displayAllPermissions;
  private final AnalyseUsedPermissions analyseUsedPermissions;
  private final FetchLibrariesForAppWithPermissions fetchLibrariesForAppWithPermissions;
  private final AppAnalyticsService appAnalyticsService;
  private final DeviceFeatures deviceFeatures;
  private CompositeSubscription subscriptions;
  private Subscription librarySubscription;
  private DetailView view;
  private App app;

  @Inject public AppDetailPresenter(AppService appService,
      IntentFactory intentFactory,
      @HasSeenEditPermissionsTutorial Preference<Boolean> hasSeenEditPermissionsTutorial,
      @DisplayAllPermissions Preference<Boolean> displayAllPermissions,
      AnalyseUsedPermissions analyseUsedPermissions,
      FetchLibrariesForAppWithPermissions fetchLibrariesForAppWithPermissions,
      AppAnalyticsService appAnalyticsService,
      DeviceFeatures deviceFeatures) {
    this.appService = appService;
    this.intentFactory = intentFactory;
    this.hasSeenEditPermissionsTutorial = hasSeenEditPermissionsTutorial;
    this.displayAllPermissions = displayAllPermissions;
    this.analyseUsedPermissions = analyseUsedPermissions;
    this.fetchLibrariesForAppWithPermissions = fetchLibrariesForAppWithPermissions;
    this.appAnalyticsService = appAnalyticsService;
    this.deviceFeatures = deviceFeatures;
  }

  public void onCreate(DetailView view, App app) {
    this.view = ensureNotNull(view);
    this.app = ensureNotNull(app);
    this.subscriptions = new CompositeSubscription();

    fetchAppUpdates();
    fetchLibraries();
    fetchAnalysisUpdates();
  }

  public void onResume() {
    subscriptions.add(analyseUsedPermissions.analyse(app)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {

        }, throwable -> {
          Timber.e(throwable, "while fetching permissions for app");
        }));
  }

  public void onDestroy() {
    this.subscriptions.clear();
    this.view = null;
  }

  private void initView(App app) {
    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());
    view.setShowAllLibraries(displayAllPermissions.get());
    view.enableEditPermissions(deviceFeatures.supportsRuntimePermissions());
  }

  private void fetchAppUpdates() {
    subscriptions.add(appService.byPackageName(app.packageName())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(app -> {
          this.app = app;
          initView(app);
        }, throwable -> Timber.d(throwable, "while fetching apps")));
  }

  private void fetchLibraries() {
    librarySubscription = fetchLibrariesForAppWithPermissions.run(app, displayAllPermissions.get())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraryWithPermissions -> {
          view.setLibraries(libraryWithPermissions);
        }, throwable -> Timber.e(throwable, "while observing libraries"));

    subscriptions.add(librarySubscription);
  }

  private void fetchAnalysisUpdates() {
    subscriptions.add(appAnalyticsService.getProgress()
        .filter(progress -> progress.app().equals(app))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(progress -> {
          Timber.d("New Progress %s: %s", progress.app().label(), progress.state());
          view.setAnalysisProgress(progress.state());
        }, Timber::e));
  }

  public void onLibraryClicked(Library library) {
    view.navigate().toLibraryDetail(library);
  }

  public void onPermissionClicked(Permission permission) {
    view.showPermissionExplanation(app, permission);
  }

  public void onAnalyseAppClicked() {
    if (!appAnalyticsService.contains(app)) {
      if (appAnalyticsService.isAnalyzing()) {
        view.showPendingApp(app);
      }
      appAnalyticsService.enqueue(app);
    } else if (app.equals(appAnalyticsService.getCurrent())) {
      view.showCancel();
    } else {
      view.showPendingApp(app);
    }
  }

  public void cancelAnalyseApp() {
    appAnalyticsService.remove(app);
  }

  public void onEditPermissionsClicked() {
    if (deviceFeatures.supportsRuntimePermissions()) {
      editPermissions();
    } else {
      view.showRuntimePermissionsTutorial(app.packageName());
    }
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

  public void openOpenAppClicked() {
    view.navigate().toApp(app.packageName());
  }

  public void onToggleShowAllPermissions(boolean isChecked) {
    if (librarySubscription != null) {
      librarySubscription.unsubscribe();
    }

    displayAllPermissions.set(isChecked);
    fetchLibraries();
  }
}
