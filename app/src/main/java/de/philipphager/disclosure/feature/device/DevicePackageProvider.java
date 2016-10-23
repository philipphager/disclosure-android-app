package de.philipphager.disclosure.feature.device;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class DevicePackageProvider {
  private final PackageManager packageManager;

  @Inject public DevicePackageProvider(PackageManager packageManager) {
    this.packageManager = packageManager;
  }

  public Observable<List<PackageInfo>> getInstalledPackages() {
    return Observable.just(packageManager.getInstalledPackages(PackageManager.GET_META_DATA));
  }
}
