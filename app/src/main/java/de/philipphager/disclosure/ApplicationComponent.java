package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.device.DeviceModule;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.overview.AppOverviewFragment;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, DeviceModule.class})
public interface ApplicationComponent {
  void inject(HomeActivity activity);

  void inject(AppOverviewFragment fragment);
}
