package de.philipphager.disclosure.feature.device;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.util.device.InternalStorageProvider;
import de.philipphager.disclosure.util.device.StorageProvider;

import static android.content.Context.NOTIFICATION_SERVICE;

@Module public class DeviceModule {
  @Provides public PackageManager providePackageManager(Context context) {
    return context.getPackageManager();
  }

  @Provides public NotificationManager provideNotificationManager(Context context) {
    return (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
  }

  @Provides public StorageProvider provideStorageProvider(Context context) {
    return new InternalStorageProvider(context);
  }

  @Provides @AndroidOsVersion public int provideOsVersion() {
    return Build.VERSION.SDK_INT;
  }
}
