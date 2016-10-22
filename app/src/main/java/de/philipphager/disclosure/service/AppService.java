package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.info.query.SelectAllAppInfos;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.query.SelectAllAppsQuery;
import de.philipphager.disclosure.database.app.query.SelectAppsByNameQuery;
import de.philipphager.disclosure.database.util.Queryable;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppService {
  private final AppRepository appRepository;
  private final Queryable<App.Info> appInfoRepository;
  private final DatabaseManager databaseManager;
  private final ToAppMapper toAppMapper;

  @Inject public AppService(AppRepository appRepository,
      Queryable<App.Info> appInfoRepository, DatabaseManager databaseManager,
      ToAppMapper toAppMapper) {
    this.appRepository = appRepository;
    this.appInfoRepository = appInfoRepository;
    this.databaseManager = databaseManager;
    this.toAppMapper = toAppMapper;
  }

  public Observable<List<App>> all() {
    try {
      BriteDatabase db = databaseManager.openObservable();
      return appRepository.query(db, new SelectAllAppsQuery());
    } finally {
      databaseManager.closeObservable();
    }
  }

  public Observable<List<App>> byName(String name) {
    try {
      BriteDatabase db = databaseManager.openObservable();
      return appRepository.query(db, new SelectAppsByNameQuery(name));
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

  public long add(PackageInfo packageInfo) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      App app = toAppMapper.map(packageInfo.applicationInfo);
      return appRepository.add(db, app);
    } finally {
      databaseManager.closeWriteable();
    }
  }

  public void removeByPackageName(String packageName) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      appRepository.remove(db, String.format("%s = '%s'", App.PACKAGENAME, packageName));
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
      }

      db.setTransactionSuccessful();
      db.endTransaction();
    } finally {
      databaseManager.closeWriteable();
    }
  }
}
