package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.api.ApiModule;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.app.detail.DetailActivity;
import de.philipphager.disclosure.feature.app.detail.tutorials.EditPermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.PermissionExplanationDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.RuntimePermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.overview.AppOverviewFragment;
import de.philipphager.disclosure.feature.app.overview.list.AppListFragment;
import de.philipphager.disclosure.feature.app.overview.search.SearchActivity;
import de.philipphager.disclosure.feature.app.overview.trusted.TrustedAppListFragment;
import de.philipphager.disclosure.feature.device.DeviceModule;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.library.category.list.LibraryListFragment;
import de.philipphager.disclosure.feature.library.category.LibraryCategoryFragment;
import de.philipphager.disclosure.feature.library.detail.LibraryDetailActivity;
import de.philipphager.disclosure.feature.preference.PreferenceModule;
import de.philipphager.disclosure.feature.settings.SettingsFragment;
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

  void inject(SearchActivity activity);

  void inject(DetailActivity activity);

  void inject(SettingsFragment settingsFragment);

  void inject(LibraryDetailActivity activity);

  void inject(LibraryListFragment fragment);

  void inject(LibraryCategoryFragment activity);

  void inject(AppOverviewFragment fragment);

  void inject(AppListFragment fragment);

  void inject(TrustedAppListFragment fragment);

  void inject(EditPermissionsTutorialDialog fragment);

  void inject(RuntimePermissionsTutorialDialog fragment);

  void inject(PermissionExplanationDialog fragment);

  void inject(PackageManagerBroadcastReceiver broadcastReceiver);
}
