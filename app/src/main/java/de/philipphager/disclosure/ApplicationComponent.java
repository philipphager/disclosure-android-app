package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.api.ApiModule;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.app.detail.DetailActivity;
import de.philipphager.disclosure.feature.device.DeviceModule;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.app.overview.AppOverviewFragment;
import de.philipphager.disclosure.feature.library.detail.LibraryOverviewActivity;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class, DatabaseModule.class, DeviceModule.class})
public interface ApplicationComponent {
  void inject(HomeActivity activity);

  void inject(DetailActivity activity);

  void inject(LibraryOverviewActivity activity);

  void inject(AppOverviewFragment fragment);
}
