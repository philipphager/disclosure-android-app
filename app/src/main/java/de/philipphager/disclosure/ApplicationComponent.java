package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.api.ApiModule;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.app.detail.AppDetailActivity;
import de.philipphager.disclosure.feature.app.detail.tutorials.EditPermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.PermissionExplanationDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.RuntimePermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.manager.AppManagerFragment;
import de.philipphager.disclosure.feature.app.manager.search.SearchActivity;
import de.philipphager.disclosure.feature.device.DeviceModule;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.library.category.LibraryCategoryFragment;
import de.philipphager.disclosure.feature.library.category.list.LibraryListFragment;
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

  void inject(AppDetailActivity activity);

  void inject(SettingsFragment settingsFragment);

  void inject(LibraryDetailActivity activity);

  void inject(LibraryListFragment fragment);

  void inject(LibraryCategoryFragment activity);

  void inject(AppManagerFragment fragment);

  void inject(EditPermissionsTutorialDialog fragment);

  void inject(RuntimePermissionsTutorialDialog fragment);

  void inject(PermissionExplanationDialog fragment);

  void inject(PackageManagerBroadcastReceiver broadcastReceiver);
}
