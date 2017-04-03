package de.philipphager.disclosure.feature.preference;

import android.content.Context;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.preference.ui.AppListSortBy;
import de.philipphager.disclosure.feature.preference.ui.DisplayAllPermissions;
import de.philipphager.disclosure.feature.preference.ui.HasSeenEditPermissionsTutorial;
import de.philipphager.disclosure.feature.preference.ui.OnlyShowUsedLibraries;
import de.philipphager.disclosure.service.sort.SortBy;
import javax.inject.Singleton;

@Module
public class PreferenceModule {
  @Provides @Singleton public SharedPreferences provideSharedPreferences(Context context) {
    return context.getSharedPreferences(context.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
  }

  @Provides @Singleton
  public RxSharedPreferences provideRxSharedPreferences(SharedPreferences sharedPreferences) {
    return RxSharedPreferences.create(sharedPreferences);
  }

  @Provides @Singleton @HasSeenEditPermissionsTutorial
  public Preference<Boolean> provideHasSeenSystemSettingsTutorial(
      RxSharedPreferences rxPreferences) {
    return rxPreferences.getBoolean("provideHasSeenSystemSettingsTutorial", false);
  }

  @Provides @Singleton @DisplayAllPermissions
  public Preference<Boolean> provideDisplayAllPermissions(RxSharedPreferences rxPreferences) {
    return rxPreferences.getBoolean("displayAllPermissions", false);
  }

  @Provides @Singleton @AppListSortBy
  public Preference<SortBy> provideAppListSortBy(RxSharedPreferences rxPreferences) {
    return rxPreferences.getEnum("appListSortBy", SortBy.LIBRARY_COUNT, SortBy.class);
  }

  @Provides @Singleton @OnlyShowUsedLibraries
  public Preference<Boolean> provideOnlyShowUsedLibraries(RxSharedPreferences rxPreferences) {
    return rxPreferences.getBoolean("onlyShowUsedLibraries", true);
  }
}
