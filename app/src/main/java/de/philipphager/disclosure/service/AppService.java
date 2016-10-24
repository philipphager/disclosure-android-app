package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.query.DeleteAppsByPackageName;
import de.philipphager.disclosure.database.app.query.SelectAllApps;
import de.philipphager.disclosure.database.app.query.SelectAppsByName;
import de.philipphager.disclosure.database.info.query.SelectAllAppInfos;
import de.philipphager.disclosure.database.util.Queryable;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.mapper.ToVersionMapper;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class AppService {
  private final Repository<App> appRepository;
  private final Queryable<App.Info> appInfoRepository;
  private final Repository<Version> versionRepository;
  private final DatabaseManager databaseManager;
  private final ToAppMapper toAppMapper;

  @Inject public AppService(Repository<App> appRepository,
      Queryable<App.Info> appInfoRepository,
      Repository<Version> versionRepository,
      DatabaseManager databaseManager,
      ToAppMapper toAppMapper) {
    this.appRepository = appRepository;
    this.appInfoRepository = appInfoRepository;
    this.versionRepository = versionRepository;
    this.databaseManager = databaseManager;
    this.toAppMapper = toAppMapper;
  }

  public Observable<List<App>> all() {
    BriteDatabase db = databaseManager.get();
    return appRepository.query(db, new SelectAllApps());
  }

  public Observable<List<App>> byName(String name) {
    BriteDatabase db = databaseManager.get();
    return appRepository.query(db, new SelectAppsByName(name));
  }

  public Observable<List<App.Info>> allAppInfos() {
    BriteDatabase db = databaseManager.get();
    return appInfoRepository.query(db, new SelectAllAppInfos());
  }

  public void add(PackageInfo packageInfo) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      App app = toAppMapper.map(packageInfo.applicationInfo);
      long appId = appRepository.add(db, app);
      Version version = new ToVersionMapper(appId).map(packageInfo);
      versionRepository.add(db, version);

      String thread = Thread.currentThread().getName();
      Timber.d("%s : inserted app %s, %s", thread, app.packageName(), version.versionName());

      transaction.markSuccessful();
    }
  }

  public void addAll(List<PackageInfo> packageInfos) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (PackageInfo packageInfo : packageInfos) {
        App app = toAppMapper.map(packageInfo.applicationInfo);
        long appId = appRepository.add(db, app);
        Version version = new ToVersionMapper(appId).map(packageInfo);
        versionRepository.add(db, version);

        String thread = Thread.currentThread().getName();
        Timber.d("%s : inserted app %s, %s", thread, app.packageName(), version.versionName());
      }

      transaction.markSuccessful();
    }
  }

  public void removeByPackageName(String packageName) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      appRepository.remove(db, new DeleteAppsByPackageName(packageName));

      String thread = Thread.currentThread().getName();
      Timber.d("%s : delete app %s", thread, packageName);

      transaction.markSuccessful();
    }
  }

  public void removeAllByPackageName(List<String> packageNames) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {

      for (String packageName : packageNames) {
        appRepository.remove(db, new DeleteAppsByPackageName(packageName));

        String thread = Thread.currentThread().getName();
        Timber.d("%s : delete app %s", thread, packageName);
      }

      transaction.markSuccessful();
    }
  }
}
