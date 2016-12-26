package de.philipphager.disclosure.feature.preference;

import android.content.Context;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.feature.preference.ui.HasSeenEditPermissionsTutorial;
import javax.inject.Singleton;

@Module
public class PreferenceModule {
  @Provides @Singleton public SharedPreferences provideSharedPreferences(Context context) {
    return context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
  }

  @Provides @Singleton
  public RxSharedPreferences provideRxSharedPreferences(SharedPreferences sharedPreferences) {
    return RxSharedPreferences.create(sharedPreferences);
  }

  @Provides @Singleton @HasSeenEditPermissionsTutorial
  public Preference<Boolean> provideHasSeenSystemSettingsTutorial(
      RxSharedPreferences rxPreferences) {
    return rxPreferences.getBoolean("show-whats-new", false);
  }
}
