package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.device.AndroidModule;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.list.AppListActivity;
import de.philipphager.disclosure.feature.list.AppOverviewFragment;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, AndroidModule.class})
public interface ApplicationComponent {
  void inject(HomeActivity activity);

  void inject(AppOverviewFragment fragment);

  void inject(AppListActivity activity);
}
