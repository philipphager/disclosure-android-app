package de.philipphager.disclosure.feature.sync;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.MockPackage;
import de.philipphager.disclosure.feature.sync.usecases.FetchOutdatedPackages;
import de.philipphager.disclosure.feature.sync.usecases.FetchUpdatedPackages;
import de.philipphager.disclosure.service.AppService;
import de.philipphager.disclosure.service.VersionService;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DBSyncerShould {
  @Mock AppRepository appRepository;
  @Mock Stopwatch stopwatch;
  @Mock FetchUpdatedPackages fetchUpdatedPackages;
  @Mock FetchOutdatedPackages fetchOutdatedApps;
  @Mock AppService appService;
  @Mock VersionService versionService;
  @InjectMocks DBSyncer dbSyncer;

  @Before public void setUp() throws Exception {

  }

  @Test public void insertUpdatedPackages() throws Exception {
    List<PackageInfo> newPackages = Arrays.asList(MockPackage.TEST, MockPackage.TEST2);

    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(newPackages));
    when(fetchOutdatedApps.get()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(appService).add(MockPackage.TEST);
    verify(appService).add(MockPackage.TEST2);
    verify(versionService).add(0, MockPackage.TEST);
    verify(versionService).add(0, MockPackage.TEST2);
    subscriber.assertCompleted();
  }

  @Test public void deleteOutdatedPackages() throws Exception {
    List<String> oldPackages = Arrays.asList(MockPackage.TEST.packageName, MockPackage.TEST2.packageName);

    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(Collections.emptyList()));
    when(fetchOutdatedApps.get()).thenReturn(Observable.just(oldPackages));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(appService).removeByPackageName(MockPackage.TEST.packageName);
    verify(appService).removeByPackageName(MockPackage.TEST2.packageName);
    subscriber.assertCompleted();
  }

  @Test public void trackSyncTime() throws Exception {
    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(Collections.emptyList()));
    when(fetchOutdatedApps.get()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(stopwatch).start();
    verify(stopwatch).stop();
  }

  @Test public void stopsTimerOnError() throws Exception {
    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(Collections.emptyList()));
    when(fetchOutdatedApps.get()).thenReturn(Observable.error(new SQLException("Test Exception")));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(stopwatch).start();
    verify(stopwatch).stop();
  }

  @Test public void syncAlwaysCompletes() throws Exception {
    List<PackageInfo> newPackages = Arrays.asList(MockPackage.TEST, MockPackage.TEST2);
    List<String> oldPackages = Arrays.asList(MockPackage.TEST.packageName, MockPackage.TEST2.packageName);

    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(newPackages));
    when(fetchOutdatedApps.get()).thenReturn(Observable.just(oldPackages));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);
    subscriber.assertCompleted();
  }

  @Test public void doUpdateBeforeRemovingOutdatedPackages() throws Exception {
    List<PackageInfo> newPackages = Arrays.asList(MockPackage.TEST, MockPackage.TEST2);

    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(newPackages));
    when(fetchOutdatedApps.get()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);
    subscriber.assertValues(2, 0);
  }
}
