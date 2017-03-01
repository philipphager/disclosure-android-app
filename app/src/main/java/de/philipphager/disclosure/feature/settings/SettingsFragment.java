package de.philipphager.disclosure.feature.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import de.philipphager.disclosure.DisclosureApp;
import de.philipphager.disclosure.R;

public class SettingsFragment extends PreferenceFragmentCompat {
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
  }

  private void inject() {
    DisclosureApp application = (DisclosureApp) getActivity().getApplication();
    application.getApplicationComponent().inject(this);
  }
}
