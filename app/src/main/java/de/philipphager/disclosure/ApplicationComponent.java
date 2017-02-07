package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.api.ApiModule;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.app.detail.DetailActivity;
import de.philipphager.disclosure.feature.app.detail.tutorials.EditPermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.RuntimePermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.overview.list.AppListFragment;
import de.philipphager.disclosure.feature.app.overview.AppOverviewFragment;
import de.philipphager.disclosure.feature.device.DeviceModule;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.library.detail.LibraryOverviewActivity;
import de.philipphager.disclosure.feature.preference.PreferenceModule;
import de.philipphager.disclosure.feature.sync.db.broadcasts.PackageManagerBroadcastReceiver;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ApplicationModule.class,
    ApiModule.class,
    DatabaseModule.class,
    DeviceModule.class,
    PreferenceModule.class
})
public interface ApplicationComponent {
  void inject(HomeActivity activity);

  void inject(DetailActivity activity);

  void inject(LibraryOverviewActivity activity);

  void inject(AppOverviewFragment fragment);

  void inject(AppListFragment fragment);

  void inject(EditPermissionsTutorialDialog fragment);

  void inject(RuntimePermissionsTutorialDialog fragment);

  void inject(PackageManagerBroadcastReceiver broadcastReceiver);
}
