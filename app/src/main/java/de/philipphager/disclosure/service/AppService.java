package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.version.VersionRepository;
import de.philipphager.disclosure.database.version.mapper.ToVersionMapper;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.util.time.Clock;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class AppService {
  private final DatabaseManager databaseManager;
  private final AppRepository appRepository;
  private final VersionRepository versionRepository;
  private final ToAppMapper toAppMapper;
  private final Clock clock;

  @Inject public AppService(DatabaseManager databaseManager,
      AppRepository appRepository,
      VersionRepository versionRepository,
      ToAppMapper toAppMapper,
      Clock clock) {
    this.databaseManager = databaseManager;
    this.appRepository = appRepository;
    this.versionRepository = versionRepository;
    this.toAppMapper = toAppMapper;
    this.clock = clock;
  }

  public Observable<List<App>> all() {
    BriteDatabase db = databaseManager.get();
    return appRepository.all(db);
  }

  public Observable<List<App>> userApps() {
    BriteDatabase db = databaseManager.get();
    return appRepository.userApps(db);
  }

  public Observable<List<App.Info>> allInfos() {
    BriteDatabase db = databaseManager.get();
    return appRepository.allInfos(db);
  }

  public Observable<List<App>> byLibrary(String libraryId) {
    BriteDatabase db = databaseManager.get();
    return appRepository.byLibrary(db, libraryId);
  }

  public void addPackage(PackageInfo packageInfo) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      App app = toAppMapper.map(packageInfo.applicationInfo);
      long appId = appRepository.insertOrUpdate(db, app);

      Version version = new ToVersionMapper(clock, appId).map(packageInfo);
      versionRepository.insertOrUpdate(db, version);

      String thread = Thread.currentThread().getName();
      Timber.d("%s : inserted app %s, %s", thread, app.packageName(), version.versionName());

      transaction.markSuccessful();
    }
  }

  public void addPackages(List<PackageInfo> packageInfos) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (PackageInfo packageInfo : packageInfos) {
        App app = toAppMapper.map(packageInfo.applicationInfo);
        long appId = appRepository.insertOrUpdate(db, app);

        Version version = new ToVersionMapper(clock, appId).map(packageInfo);
        versionRepository.insert(db, version);

        String thread = Thread.currentThread().getName();
        Timber.d("%s : inserted app %s, %s", thread, app.packageName(), version.versionName());
      }

      transaction.markSuccessful();
    }
  }

  public void removeAllByPackageName(List<String> packageNames) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (String packageName : packageNames) {
        String where = String.format("%s = '%s'", App.PACKAGENAME, packageName);
        appRepository.delete(db, where);

        String thread = Thread.currentThread().getName();
        Timber.d("%s : delete app %s", thread, packageName);
      }

      transaction.markSuccessful();
    }
  }
}
