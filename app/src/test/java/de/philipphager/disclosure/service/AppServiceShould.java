package de.philipphager.disclosure.service;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.MockApp;
import de.philipphager.disclosure.database.app.MockPackage;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.query.SelectAllApps;
import de.philipphager.disclosure.database.app.query.SelectAppsByName;
import de.philipphager.disclosure.database.info.query.SelectAllAppInfos;
import de.philipphager.disclosure.database.util.Queryable;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BriteDatabase.class, SQLiteDatabase.class})
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class AppServiceShould {
  @Mock protected Repository<App> appRepository;
  @Mock protected Queryable<App.Info> appInfoRepository;
  @Mock protected Repository<Version> versionRepository;
  @Mock protected DatabaseManager databaseManager;
  @Mock protected ToAppMapper toAppMapper;
  private AppService appService;
  private SQLiteDatabase writeableDB;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);

    writeableDB = PowerMockito.mock(SQLiteDatabase.class);

    when(databaseManager.openWriteable()).thenReturn(writeableDB).thenReturn(null);

    appService = new AppService(appRepository, appInfoRepository,
        versionRepository, databaseManager, toAppMapper);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchAllApps() {
    List<App> testApps = Arrays.asList(MockApp.TEST, MockApp.TEST2);
    when(appRepository.query(any(BriteDatabase.class), any(SelectAllApps.class)))
        .thenReturn(Observable.just(testApps));

    TestSubscriber<List<App>> subscriber = new TestSubscriber<>();

    appService.all()
        .toBlocking()
        .subscribe(subscriber);

    verify(databaseManager).openObservable();
    verify(databaseManager).closeObservable();
    subscriber.assertReceivedOnNext(Collections.singletonList(testApps));
    subscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchAllAppsByName() {
    List<App> testApps = Arrays.asList(MockApp.TEST2);
    when(appRepository.query(any(BriteDatabase.class), any(SelectAppsByName.class)))
        .thenReturn(Observable.just(testApps));

    TestSubscriber<List<App>> subscriber = new TestSubscriber<>();

    appService.byName("test")
        .toBlocking()
        .subscribe(subscriber);

    verify(databaseManager).openObservable();
    verify(databaseManager).closeObservable();
    subscriber.assertReceivedOnNext(Collections.singletonList(testApps));
    subscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchAllAppInfos() {
    List<App.Info> testInfos = Arrays.asList(MockApp.TEST_INFO, MockApp.TEST2_INFO);
    when(appInfoRepository.query(any(BriteDatabase.class), any(SelectAllAppInfos.class)))
        .thenReturn(Observable.just(testInfos));

    TestSubscriber<List<App.Info>> subscriber = new TestSubscriber<>();

    appService.allAppInfos()
        .toBlocking()
        .subscribe(subscriber);

    verify(databaseManager).openObservable();
    verify(databaseManager).closeObservable();
    subscriber.assertReceivedOnNext(Collections.singletonList(testInfos));
    subscriber.assertCompleted();
  }
}
