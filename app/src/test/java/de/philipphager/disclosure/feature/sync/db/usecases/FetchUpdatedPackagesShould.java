package de.philipphager.disclosure.feature.sync.db.usecases;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.mapper.ToInfoMapper;
import de.philipphager.disclosure.database.app.model.AppInfo;
import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockPackageInfo;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.AppService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class FetchUpdatedPackagesShould {
  @Mock protected DevicePackageProvider appProvider;
  @Mock protected AppService appService;
  @Mock protected ToInfoMapper toInfoMapper;
  @InjectMocks protected FetchUpdatedPackages fetchUpdatedPackages;

  @Before public void setUp() {
    when(toInfoMapper.map(MockPackageInfo.TEST)).thenReturn(MockApp.TEST_INFO);
    when(toInfoMapper.map(MockPackageInfo.TEST2)).thenReturn(MockApp.TEST2_INFO);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchNoAppsIfNoneInstalledOnDevice() {
    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(Collections.emptyList()));
    when(appService.allInfos()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<List<PackageInfo>> testSubscriber = new TestSubscriber<>();
    fetchUpdatedPackages.get().toBlocking().subscribe(testSubscriber);

    testSubscriber.assertReceivedOnNext(Collections.singletonList(Collections.emptyList()));
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchAllAppsFromDeviceIfNoneAreSavedInDB() {
    List<PackageInfo> installedPackages =
        Arrays.asList(MockPackageInfo.TEST, MockPackageInfo.TEST2);

    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(installedPackages));
    when(appService.allInfos()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<List<PackageInfo>> testSubscriber = new TestSubscriber<>();
    fetchUpdatedPackages.get().toBlocking().subscribe(testSubscriber);

    List<PackageInfo> expectedPackages = Arrays.asList(MockPackageInfo.TEST, MockPackageInfo.TEST2);
    List<PackageInfo> receivedPackages = testSubscriber.getOnNextEvents().get(0);
    assertThat(receivedPackages).containsAll(expectedPackages);
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchOnlyNewAppsFromDevice() {
    List<PackageInfo> installedPackages =
        Arrays.asList(MockPackageInfo.TEST, MockPackageInfo.TEST2);
    List<AppInfo> savedPackages = Collections.singletonList(MockApp.TEST_INFO);

    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(installedPackages));
    when(appService.allInfos()).thenReturn(Observable.just(savedPackages));

    TestSubscriber<List<PackageInfo>> testSubscriber = new TestSubscriber<>();
    fetchUpdatedPackages.get().toBlocking().subscribe(testSubscriber);

    List<PackageInfo> expectedPackages = Collections.singletonList(MockPackageInfo.TEST2);
    List<PackageInfo> receivedPackages = testSubscriber.getOnNextEvents().get(0);
    assertThat(receivedPackages).containsAll(expectedPackages);
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchUpdatedAppVersions() {
    int newVersion = MockPackageInfo.TEST.versionCode + 1;

    PackageInfo testPackageVersion2 = MockPackageInfo.TEST;
    testPackageVersion2.versionCode = newVersion;
    AppInfo testAppInfoVersion2 = AppInfo.create(testPackageVersion2.packageName, newVersion);

    List<PackageInfo> installedPackages = Collections.singletonList(testPackageVersion2);
    List<AppInfo> savedPackages = Collections.singletonList(MockApp.TEST_INFO);

    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(installedPackages));
    when(appService.allInfos()).thenReturn(Observable.just(savedPackages));
    when(toInfoMapper.map(testPackageVersion2)).thenReturn(testAppInfoVersion2);

    TestSubscriber<List<PackageInfo>> testSubscriber = new TestSubscriber<>();
    fetchUpdatedPackages.get().toBlocking().subscribe(testSubscriber);

    List<PackageInfo> expectedPackages = Collections.singletonList(testPackageVersion2);
    List<PackageInfo> receivedPackages = testSubscriber.getOnNextEvents().get(0);
    assertThat(receivedPackages).containsAll(expectedPackages);
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void alwaysCompleteAFetch() {
    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(Collections.emptyList()));
    when(appService.allInfos()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<List<PackageInfo>> testSubscriber = new TestSubscriber<>();
    fetchUpdatedPackages.get().toBlocking().subscribe(testSubscriber);

    testSubscriber.assertCompleted();
  }
}
