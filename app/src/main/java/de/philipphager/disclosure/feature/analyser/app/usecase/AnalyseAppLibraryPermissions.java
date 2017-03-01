package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.analyser.library.usecase.AnalyseLibraryMethodInvocations;
import de.philipphager.disclosure.feature.analyser.permission.usecase.AnalysePermissionsFromMethodInvocations;
import de.philipphager.disclosure.feature.app.detail.AnalysisProgressView;
import de.philipphager.disclosure.service.LibraryService;
import de.philipphager.disclosure.service.PermissionService;
import de.philipphager.disclosure.service.app.AppService;
import de.philipphager.disclosure.util.device.FileUtils;
import de.philipphager.disclosure.util.device.StorageProvider;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.LocalDateTime;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

import static de.philipphager.disclosure.util.rx.RxUtils.zipMap;

public class AnalyseAppLibraryPermissions {
  private final DecompileApp decompileApp;
  private final AnalyseLibraryMethodInvocations analyseLibraryMethodInvocations;
  private final AnalysePermissionsFromMethodInvocations analysePermissionsFromMethodInvocations;
  private final LibraryService libraryService;
  private final PermissionService permissionService;
  private final StorageProvider storageProvider;
  private final FileUtils fileUtils;
  private final AppService appService;
  private final PublishSubject<AnalysisProgressView.State> progressSubject;

  @Inject public AnalyseAppLibraryPermissions(DecompileApp decompileApp,
      AnalyseLibraryMethodInvocations analyseLibraryMethodInvocations,
      AnalysePermissionsFromMethodInvocations analysePermissionsFromMethodInvocations,
      LibraryService libraryService,
      PermissionService permissionService,
      StorageProvider storageProvider,
      FileUtils fileUtils,
      AppService appService) {
    this.decompileApp = decompileApp;
    this.analyseLibraryMethodInvocations = analyseLibraryMethodInvocations;
    this.analysePermissionsFromMethodInvocations = analysePermissionsFromMethodInvocations;
    this.libraryService = libraryService;
    this.permissionService = permissionService;
    this.storageProvider = storageProvider;
    this.fileUtils = fileUtils;
    this.appService = appService;
    this.progressSubject = PublishSubject.create();
  }

  public Observable<List<Permission>> run(App app) {
    File compiledApkDir = getOutputDirForApp(app);
    progressSubject.onNext(AnalysisProgressView.State.DECOMPILATION);

    return zipMap(decompileApp.run(app, compiledApkDir), libraryService.byApp(app),
        (apkDir, libraries) -> {
          progressSubject.onNext(AnalysisProgressView.State.EXTRACTION);
          return Observable.from(libraries)
              .subscribeOn(Schedulers.computation())
              .flatMap(library -> analyseLibraryMethodInvocations.run(apkDir, library)
                  .subscribeOn(Schedulers.computation())
                  .flatMap((invokedMethods) -> {
                    progressSubject.onNext(AnalysisProgressView.State.ANALYSIS);
                    return analysePermissionsFromMethodInvocations.run(invokedMethods);
                  })
                  .doOnNext(permissions -> Timber.d("Found %s for library %s", permissions, library))
                  .doOnNext(permissions -> permissionService.insertForAppAndLibrary(app, library, permissions))
              );
        }).doOnTerminate(() -> {

      appService.insertOrUpdate(app.withAnalyzedAt(LocalDateTime.now()));
      deleteCompiledApk(compiledApkDir);
    });
  }

  public Observable<AnalysisProgressView.State> getProgress() {
    return progressSubject;
  }

  private File getOutputDirForApp(App app) {
    File rootDir = storageProvider.getRootDir();
    return new File(rootDir.getPath() + File.separator + app.packageName());
  }

  private void deleteCompiledApk(File apkDir) {
    fileUtils.delete(apkDir);
  }
}
