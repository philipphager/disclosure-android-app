package de.philipphager.disclosure.feature.device;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class DevicePackageProvider {
  private final PackageManager packageManager;

  @Inject public DevicePackageProvider(PackageManager packageManager) {
    this.packageManager = packageManager;
  }

  public Observable<List<PackageInfo>> getInstalledPackages() {
    return Observable.just(packageManager.getInstalledPackages(
        PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS));
  }

  public Observable<PackageInfo> getPackageInfo(String packageName) {
    return Observable.fromCallable(() ->
        packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS));
  }

  public Observable<PermissionInfo> getPermissionInfo(String id) {
    return Observable.fromCallable(
        () -> packageManager.getPermissionInfo(id, PackageManager.GET_META_DATA));
  }
}
