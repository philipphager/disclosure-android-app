package de.philipphager.disclosure.feature.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Toast;
import de.philipphager.disclosure.BuildConfig;
import de.philipphager.disclosure.DisclosureApp;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.feature.navigation.Navigator;
import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat implements Navigates {
  @Inject protected Navigator navigator;

  public static SettingsFragment newInstance() {
    return new SettingsFragment();
  }

  @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    getPreferenceManager().setSharedPreferencesName(getString(R.string.shared_preferences));
    setPreferencesFromResource(R.xml.settings, rootKey);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    inject();

    setupContact();
    setupLicense();
    setupVersion();
  }

  @Override public Navigator navigate() {
    return navigator;
  }

  private void inject() {
    DisclosureApp application = (DisclosureApp) getActivity().getApplication();
    application.getApplicationComponent().inject(this);
    navigator.setActivity(getActivity());
  }

  private void setupContact() {
    Preference preference = findPreference(getString(R.string.settings_contact));
    preference.setOnPreferenceClickListener(pref -> {
      navigate().toNestedSystemSetting(NestedSettingsActivity.CONTACT);
      return true;
    });
  }

  private void setupLicense() {
    Preference preference = findPreference(getString(R.string.settings_license_and_thanks));
    preference.setOnPreferenceClickListener(pref -> {
      navigate().toNestedSystemSetting(NestedSettingsActivity.LICENSE);
      return true;
    });
  }

  private void setupVersion() {
    Preference preference = findPreference(getString(R.string.settings_version));
    preference.setSummary(BuildConfig.VERSION_NAME);
  }
}
