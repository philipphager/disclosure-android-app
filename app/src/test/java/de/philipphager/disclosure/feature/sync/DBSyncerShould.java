package de.philipphager.disclosure.feature.sync;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.mocks.MockPackageInfo;
import de.philipphager.disclosure.feature.sync.db.DBSyncer;
import de.philipphager.disclosure.feature.sync.db.usecases.FetchOutdatedPackages;
import de.philipphager.disclosure.feature.sync.db.usecases.FetchUpdatedPackages;
import de.philipphager.disclosure.service.AppService;
import de.philipphager.disclosure.util.time.Stopwatch;
import java.util.ArrayList;
import java.util.Arrays;
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
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class DBSyncerShould {
  @Mock protected Stopwatch stopwatch;
  @Mock protected FetchUpdatedPackages fetchUpdatedPackages;
  @Mock protected FetchOutdatedPackages fetchOutdatedApps;
  @Mock protected AppService appService;
  @InjectMocks protected DBSyncer dbSyncer;
  private List<PackageInfo> newPackages;
  private List<String> oldPackages;

  @Before public void setUp() {
    newPackages = new ArrayList<>(0);
    oldPackages = new ArrayList<>(0);

    when(fetchUpdatedPackages.get()).thenReturn(Observable.just(newPackages));
    when(fetchOutdatedApps.get()).thenReturn(Observable.just(oldPackages));
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertUpdatedPackages() {
    newPackages.addAll(Arrays.asList(MockPackageInfo.TEST, MockPackageInfo.TEST2));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    List<PackageInfo> expectedPackages = Arrays.asList(MockPackageInfo.TEST, MockPackageInfo.TEST2);
    verify(appService).addPackages(expectedPackages);
    subscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void deleteOutdatedPackages() {
    oldPackages.addAll(Arrays.asList(MockPackageInfo.TEST.packageName, MockPackageInfo.TEST2.packageName));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    List<String> expectedPackages =
        Arrays.asList(MockPackageInfo.TEST.packageName, MockPackageInfo.TEST2.packageName);
    verify(appService).removeAllByPackageName(expectedPackages);
    subscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void trackSyncTime() {
    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(stopwatch).start();
    verify(stopwatch).stop();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void stopsTimerOnError() {
    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(stopwatch).start();
    verify(stopwatch).stop();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void syncAlwaysCompletes() {
    newPackages.addAll(Arrays.asList(MockPackageInfo.TEST, MockPackageInfo.TEST2));
    oldPackages.addAll(Arrays.asList(MockPackageInfo.TEST.packageName, MockPackageInfo.TEST2.packageName));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);
    subscriber.assertCompleted();
  }
}
