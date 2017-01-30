package de.philipphager.disclosure.feature.app.detail;

import android.content.Intent;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedLibraries;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedPermissions;
import de.philipphager.disclosure.feature.analyser.library.usecases.AnalyseUsedPermissionsInLibraries;
import de.philipphager.disclosure.feature.preference.ui.HasSeenEditPermissionsTutorial;
import de.philipphager.disclosure.service.LibraryService;
import de.philipphager.disclosure.util.device.IntentFactory;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;
import static de.philipphager.disclosure.util.device.DeviceFeatures.supportsRuntimePermissions;

public class DetailPresenter {
  private static final int UNINSTALL_REQUEST_CODE = 39857;

  private final AnalyseUsedLibraries analyseUsedLibraries;
  private final LibraryService libraryService;
  private final IntentFactory intentFactory;
  private final AnalyseUsedPermissions analyseUsedPermissions;
  private final AnalyseUsedPermissionsInLibraries analyseUsedPermissionsInLibraries;
  private final Preference<Boolean> hasSeenEditPermissionsTutorial;
  private CompositeSubscription subscriptions;
  private DetailView view;
  private App app;

  @Inject public DetailPresenter(AnalyseUsedLibraries analyseUsedLibraries,
      LibraryService libraryService,
      IntentFactory intentFactory,
      AnalyseUsedPermissions analyseUsedPermissions,
      AnalyseUsedPermissionsInLibraries analyseUsedPermissionsInLibraries,
      @HasSeenEditPermissionsTutorial Preference<Boolean> hasSeenEditPermissionsTutorial) {
    this.analyseUsedLibraries = analyseUsedLibraries;
    this.libraryService = libraryService;
    this.intentFactory = intentFactory;
    this.analyseUsedPermissions = analyseUsedPermissions;
    this.analyseUsedPermissionsInLibraries = analyseUsedPermissionsInLibraries;
    this.hasSeenEditPermissionsTutorial = hasSeenEditPermissionsTutorial;
  }

  public void onCreate(DetailView view, App app) {
    this.view = ensureNotNull(view);
    this.app = ensureNotNull(app);
    this.subscriptions = new CompositeSubscription();

    initView();
    loadLibraries();
  }

  public void onDestroy() {
    subscriptions.unsubscribe();
  }

  private void initView() {
    view.setToolbarTitle(app.label());
    view.setAppIcon(app.packageName());
    view.enableEditPermissions(supportsRuntimePermissions());
  }

  private void loadLibraries() {
    subscriptions.add(libraryService.byApp(app)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraries -> {
          Timber.d("loaded %s libraries", libraries);
          view.setLibraries(libraries);
        }, Timber::e));
  }

  public void onLibraryClicked(Library library) {
    view.navigate().toLibraryOverview(library);
  }

  public void onAnalyseClicked() {
    subscriptions.add(analyseUsedLibraries.analyse(app)
        .subscribe(libraries -> {
          Timber.d("found %s libraries", libraries);
        }, Timber::e));

    //subscriptions.add(analyseUsedPermissions.analyse(app)
    //.subscribe(permissions -> view.notify("found " + permissions.size() + " permissions"),
    //    Timber::e));

    subscriptions.add(analyseUsedPermissionsInLibraries.analyse(app)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(permissions -> view.notify("found " + permissions.size() + " methods"),
            Timber::e));
  }

  public void onEditPermissionsClicked() {
    if (supportsRuntimePermissions()) {
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
}
