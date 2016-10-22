package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.mapper.ToVersionMapper;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.database.version.query.SelectAllVersionsQuery;
import de.philipphager.disclosure.database.version.query.SelectVersionByAppQuery;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionService {
  private final Repository<Version> versionRepository;
  private final DatabaseManager databaseManager;

  @Inject
  public VersionService(Repository<Version> versionRepository, DatabaseManager databaseManager) {
    this.versionRepository = versionRepository;
    this.databaseManager = databaseManager;
  }

  public Observable<List<Version>> all() {
    try {
      BriteDatabase db = databaseManager.openObservable();
      return versionRepository.query(db, new SelectAllVersionsQuery());
    } finally {
      databaseManager.closeObservable();
    }
  }

  public Observable<List<Version>> byApp(long appId) {
    try {
      BriteDatabase db = databaseManager.openObservable();
      return versionRepository.query(db, new SelectVersionByAppQuery(appId));
    } finally {
      databaseManager.closeObservable();
    }
  }

  public long add(long appId, PackageInfo packageInfo) {
    try {
      SQLiteDatabase db = databaseManager.openWriteable();
      Version version = new ToVersionMapper(appId).map(packageInfo);
      return versionRepository.add(db, version);
    } finally {
      databaseManager.closeWriteable();
    }
  }
}
