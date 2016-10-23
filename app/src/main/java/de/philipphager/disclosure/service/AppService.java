package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.info.query.SelectAllAppInfos;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.query.SelectAllApps;
import de.philipphager.disclosure.database.app.query.SelectAppsByName;
import de.philipphager.disclosure.database.util.Queryable;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.mapper.ToVersionMapper;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public class AppService {
  private final AppRepository appRepository;
  private final Queryable<App.Info> appInfoRepository;
  private final Repository<Version> versionRepository;
  private final DatabaseManager databaseManager;
  private final ToAppMapper toAppMapper;

  @Inject public AppService(AppRepository appRepository,
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
    try {
      BriteDatabase db = databaseManager.openObservable();
      return appRepository.query(db, new SelectAllApps());
    } finally {
      databaseManager.closeObservable();
    }
  }

  public Observable<List<App>> byName(String name) {
    try {
      BriteDatabase db = databaseManager.openObservable();
      return appRepository.query(db, new SelectAppsByName(name));
    } finally {
      databaseManager.closeObservable();
    }
  }

  public Observable<List<App.Info>> allAppInfos() {
    try {
      BriteDatabase db = databaseManager.openObservable();
      return appInfoRepository.query(db, new SelectAllAppInfos());
    } finally {
      databaseManager.closeObservable();
    }
  }

  public void add(PackageInfo packageInfo) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      App app = toAppMapper.map(packageInfo.applicationInfo);
      long appId = appRepository.add(db, app);
      Version version = new ToVersionMapper(appId).map(packageInfo);
      versionRepository.add(db, version);

      String thread = Thread.currentThread().getName();
      Timber.d("%s : inserted app %s, %s", thread, app.packageName(), version.versionName());
    } finally {
      databaseManager.closeWriteable();
    }
  }

  public void addAll(List<PackageInfo> packageInfos) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      db.beginTransaction();

      for (PackageInfo packageInfo : packageInfos) {
        App app = toAppMapper.map(packageInfo.applicationInfo);
        long appId = appRepository.add(db, app);
        Version version = new ToVersionMapper(appId).map(packageInfo);
        versionRepository.add(db, version);

        String thread = Thread.currentThread().getName();
        Timber.d("%s : inserted app %s, %s", thread, app.packageName(), version.versionName());
      }

      db.setTransactionSuccessful();
      db.endTransaction();
    } finally {
      databaseManager.closeWriteable();
    }
  }

  public void removeByPackageName(String packageName) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      appRepository.remove(db, String.format("%s = '%s'", App.PACKAGENAME, packageName));

      String thread = Thread.currentThread().getName();
      Timber.d("%s : delete app %s", thread, packageName);
    } finally {
      databaseManager.closeWriteable();
    }
  }

  public void removeAllByPackageName(List<String> packageNames) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      db.beginTransaction();

      for (String packageName : packageNames) {
        appRepository.remove(db, String.format("%s = '%s'", App.PACKAGENAME, packageName));

        String thread = Thread.currentThread().getName();
        Timber.d("%s : delete app %s", thread, packageName);
      }

      db.setTransactionSuccessful();
      db.endTransaction();
    } finally {
      databaseManager.closeWriteable();
    }
  }
}
