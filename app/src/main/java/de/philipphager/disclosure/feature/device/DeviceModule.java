package de.philipphager.disclosure.feature.device;

import android.content.Context;
import android.content.pm.PackageManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class DeviceModule {
  @Provides @Singleton public PackageManager providePackageManager(Context context) {
    return context.getPackageManager();
  }
}
