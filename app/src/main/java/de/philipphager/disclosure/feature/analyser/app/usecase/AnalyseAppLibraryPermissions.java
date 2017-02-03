package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.analyser.library.usecase.AnalyseLibraryMethodInvocations;
import de.philipphager.disclosure.feature.analyser.permission.usecase.AnalysePermissionsFromMethodInvocations;
import de.philipphager.disclosure.service.LibraryService;
import de.philipphager.disclosure.service.PermissionService;
import de.philipphager.disclosure.util.device.FileUtils;
import de.philipphager.disclosure.util.device.StorageProvider;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static de.philipphager.disclosure.util.rx.RxUtils.zipMap;

public class AnalyseAppLibraryPermissions {
  private final DecompileApk decompileApk;
  private final AnalyseLibraryMethodInvocations analyseLibraryMethodInvocations;
  private final AnalysePermissionsFromMethodInvocations analysePermissionsFromMethodInvocations;
  private final LibraryService libraryService;
  private final PermissionService permissionService;
  private final StorageProvider storageProvider;
  private final FileUtils fileUtils;

  @Inject public AnalyseAppLibraryPermissions(DecompileApk decompileApk,
      AnalyseLibraryMethodInvocations analyseLibraryMethodInvocations,
      AnalysePermissionsFromMethodInvocations analysePermissionsFromMethodInvocations,
      LibraryService libraryService,
      PermissionService permissionService,
      StorageProvider storageProvider,
      FileUtils fileUtils) {
    this.decompileApk = decompileApk;
    this.analyseLibraryMethodInvocations = analyseLibraryMethodInvocations;
    this.analysePermissionsFromMethodInvocations = analysePermissionsFromMethodInvocations;
    this.libraryService = libraryService;
    this.permissionService = permissionService;
    this.storageProvider = storageProvider;
    this.fileUtils = fileUtils;
  }

  public Observable<List<Permission>> run(App app) {
    File compiledApkDir = getOutputDirForApp(app);

    return zipMap(decompileApk.run(app, compiledApkDir), libraryService.byApp(app), (apkDir, libraries) ->
        Observable.from(libraries)
            .subscribeOn(Schedulers.computation())
            .flatMap(library -> analyseLibraryMethodInvocations.run(apkDir, library)
                .subscribeOn(Schedulers.computation())
                .flatMap(analysePermissionsFromMethodInvocations::run)
                .doOnNext(permissions -> Timber.d("Found %s for library %s", permissions, library))
                .doOnNext(permissions -> permissionService.insertForAppAndLibrary(app, library, permissions))
            )).doOnTerminate(() -> deleteCompiledApk(compiledApkDir));
  }

  private File getOutputDirForApp(App app) {
    File rootDir = storageProvider.getRootDir();
    return new File(rootDir.getPath() + File.separator + app.packageName());
  }

  private void deleteCompiledApk(File apkDir) {
    fileUtils.delete(apkDir);
  }
}
