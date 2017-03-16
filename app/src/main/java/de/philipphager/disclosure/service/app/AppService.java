package de.philipphager.disclosure.service.app;

import android.content.pm.PackageInfo;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.database.app.model.AppWithPermissions;
import de.philipphager.disclosure.database.version.VersionRepository;
import de.philipphager.disclosure.database.version.mapper.ToVersionMapper;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.service.app.filter.SortBy;
import de.philipphager.disclosure.service.app.filter.SortByAnalyzedAt;
import de.philipphager.disclosure.service.app.filter.SortByLibraryCount;
import de.philipphager.disclosure.service.app.filter.SortByName;
import de.philipphager.disclosure.service.app.filter.SortByPermissionCount;
import de.philipphager.disclosure.util.time.Clock;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func2;
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

  public Observable<List<App.Info>> allInfos() {
    BriteDatabase db = databaseManager.get();
    return appRepository.allInfos(db);
  }

  public Observable<List<AppReport>> allReports(SortBy sortBy) {
    BriteDatabase db = databaseManager.get();
    return appRepository.selectReport(db)
        .flatMap(appReports -> Observable.from(appReports)
            .toSortedList(getSortingFunction(sortBy)));
  }

  public Observable<List<AppReport>> search(String query) {
    BriteDatabase db = databaseManager.get();
    return appRepository.selectByQuery(db, query);
  }

  public Observable<App> byPackageName(String packageName) {
    BriteDatabase db = databaseManager.get();
    return appRepository.byPackageName(db, packageName);
  }

  public Observable<List<App>> byLibrary(String libraryId) {
    BriteDatabase db = databaseManager.get();
    return appRepository.byLibrary(db, libraryId);
  }

  public Observable<List<AppWithPermissions>> byLibraryWithPermissions(String libraryId) {
    BriteDatabase db = databaseManager.get();
    return appRepository.byLibraryWithPermissionCount(db, libraryId);
  }

  public Observable<List<App>> byFeature(String featureId) {
    BriteDatabase db = databaseManager.get();
    return appRepository.byFeature(db, featureId);
  }

  public void insertOrUpdate(App app) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      appRepository.insertOrUpdate(db, app);
      transaction.markSuccessful();
    }
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

  public Func2<AppReport, AppReport, Integer> getSortingFunction(SortBy sortBy) {
    switch (sortBy) {
      case NAME:
        return new SortByName();
      case LIBRARY_COUNT:
        return new SortByLibraryCount();
      case ANALYZED_AT:
        return new SortByAnalyzedAt();
      case PERMISSION_COUNT:
        return new SortByPermissionCount();
      default:
        throw new IllegalArgumentException("no sorting function for %s " + sortBy);
    }
  }
}
