package de.philipphager.disclosure.feature.sync;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.AppRepository;
import de.philipphager.disclosure.database.app.MockPackage;
import de.philipphager.disclosure.feature.sync.usecases.FetchOutdatedPackages;
import de.philipphager.disclosure.feature.sync.usecases.FetchUpdatedPackages;
import de.philipphager.disclosure.service.AppService;
import de.philipphager.disclosure.service.VersionService;
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
  @Mock protected AppRepository appRepository;
  @Mock protected Stopwatch stopwatch;
  @Mock protected FetchUpdatedPackages fetchUpdatedPackages;
  @Mock protected FetchOutdatedPackages fetchOutdatedApps;
  @Mock protected AppService appService;
  @Mock protected VersionService versionService;
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
    newPackages.addAll(Arrays.asList(MockPackage.test(), MockPackage.test2()));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(appService).add(MockPackage.test());
    verify(appService).add(MockPackage.test2());
    verify(versionService).add(0, MockPackage.test());
    verify(versionService).add(0, MockPackage.test2());
    subscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void deleteOutdatedPackages() {
    oldPackages.addAll(Arrays.asList(MockPackage.test().packageName, MockPackage.test2().packageName));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);

    verify(appService).removeByPackageName(MockPackage.test().packageName);
    verify(appService).removeByPackageName(MockPackage.test2().packageName);
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
    newPackages.addAll(Arrays.asList(MockPackage.test(), MockPackage.test2()));
    oldPackages.addAll(Arrays.asList(MockPackage.test().packageName, MockPackage.test2().packageName));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);
    subscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void doUpdateBeforeRemovingOutdatedPackages() {
    newPackages.addAll(Arrays.asList(MockPackage.test(), MockPackage.test2()));

    TestSubscriber<Integer> subscriber = new TestSubscriber<>();
    dbSyncer.sync().toBlocking().subscribe(subscriber);
    subscriber.assertValues(2, 0);
  }
}
