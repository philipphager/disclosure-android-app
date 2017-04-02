package de.philipphager.disclosure.feature.sync.db.usecases;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.mocks.MockPackageInfo;
import de.philipphager.disclosure.feature.device.DevicePackageProvider;
import de.philipphager.disclosure.service.app.AppService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class FetchOutdatedPackagesShould {
  @Mock protected DevicePackageProvider appProvider;
  @Mock protected AppService appService;
  @InjectMocks protected FetchOutdatedPackages fetchOutdatedPackages;
  private App.Info facebookInfo;
  private App.Info facebookInfoVersion2;
  private App.Info instagramInfo;

  @Before public void setUp() throws Exception {
    facebookInfo =
        App.Info.create(MockPackageInfo.TEST.packageName, MockPackageInfo.TEST.versionCode);
    facebookInfoVersion2 =
        App.Info.create(MockPackageInfo.TEST.packageName, 2);
    instagramInfo =
        App.Info.create(MockPackageInfo.TEST2.packageName, MockPackageInfo.TEST2.versionCode);
  }

  @Test
  public void loadNoPackagesIfNoneAreInstalled() {
    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(Collections.emptyList()));
    when(appService.allInfos()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<List<String>> subscriber = new TestSubscriber<>();
    fetchOutdatedPackages.get().subscribe(subscriber);

    List<String> noPackages = Collections.emptyList();

    subscriber.assertReceivedOnNext(Collections.singletonList(noPackages));
    subscriber.assertCompleted();
  }

  @Test
  public void fetchAllOutdatedPackagesThatWereUninstalled() {
    List<App.Info> savedPackages = Arrays.asList(facebookInfo, instagramInfo);

    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(Collections.emptyList()));
    when(appService.allInfos()).thenReturn(Observable.just(savedPackages));

    TestSubscriber<List<String>> subscriber = new TestSubscriber<>();
    fetchOutdatedPackages.get().toBlocking().subscribe(subscriber);

    List<String> expectedPackages =
        Arrays.asList(facebookInfo.packageName(), instagramInfo.packageName());

    List<String> receivedPackages = subscriber.getOnNextEvents().get(0);
    Assertions.assertThat(receivedPackages).containsAll(expectedPackages);
    subscriber.assertCompleted();
  }

  @Test
  public void doNotFetchPackagesThatAreStillInstalled() {
    List<App.Info> savedPackages = Collections.singletonList(facebookInfo);
    List<PackageInfo> installedPackages = Collections.singletonList(MockPackageInfo.TEST);

    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(installedPackages));
    when(appService.allInfos()).thenReturn(Observable.just(savedPackages));

    TestSubscriber<List<String>> subscriber = new TestSubscriber<>();
    fetchOutdatedPackages.get().toBlocking().subscribe(subscriber);

    List<String> expectedPackages = Collections.emptyList();

    List<String> receivedPackages = subscriber.getOnNextEvents().get(0);
    Assertions.assertThat(receivedPackages).containsAll(expectedPackages);
    subscriber.assertCompleted();
  }

  @Test
  public void fetchOnlyOnePackageIfMoreVersionsOfAnAppHaveBeenSaved() {
    List<App.Info> savedPackages = Arrays.asList(facebookInfo, facebookInfoVersion2);
    List<PackageInfo> installedPackages = Collections.emptyList();

    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(installedPackages));
    when(appService.allInfos()).thenReturn(Observable.just(savedPackages));

    TestSubscriber<List<String>> subscriber = new TestSubscriber<>();
    fetchOutdatedPackages.get().toBlocking().subscribe(subscriber);

    List<String> expectedPackages = Collections.singletonList(facebookInfo.packageName());

    List<String> receivedPackages = subscriber.getOnNextEvents().get(0);
    Assertions.assertThat(receivedPackages).containsAll(expectedPackages);
    subscriber.assertCompleted();
  }

  @Test
  public void alwaysCompleteAFetch() {
    when(appProvider.getInstalledPackages()).thenReturn(Observable.just(Collections.emptyList()));
    when(appService.allInfos()).thenReturn(Observable.just(Collections.emptyList()));

    TestSubscriber<List<String>> subscriber = new TestSubscriber<>();
    fetchOutdatedPackages.get().subscribe(subscriber);

    subscriber.assertCompleted();
  }
}
