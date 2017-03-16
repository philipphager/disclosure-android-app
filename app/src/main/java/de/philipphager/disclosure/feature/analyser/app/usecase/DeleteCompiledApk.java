package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.device.FileUtils;
import de.philipphager.disclosure.util.device.StorageProvider;
import java.io.File;
import javax.inject.Inject;
import rx.Observable;

public class DeleteCompiledApk {
  private final StorageProvider storageProvider;
  private final FileUtils fileUtils;

  @Inject public DeleteCompiledApk(StorageProvider storageProvider,
      FileUtils fileUtils) {
    this.storageProvider = storageProvider;
    this.fileUtils = fileUtils;
  }

  public Observable<File> run(App app) {
    return Observable.fromCallable(() -> {
      File compiledApkDir = getOutputDirForApp(app);
      fileUtils.delete(compiledApkDir);
      return compiledApkDir;
    });
  }

  private File getOutputDirForApp(App app) {
    File rootDir = storageProvider.getRootDir();
    return new File(rootDir.getPath() + File.separator + app.packageName());
  }
}
