package de.philipphager.disclosure.feature.settings.nested;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import de.philipphager.disclosure.R;

public class LicenseFragment extends PreferenceFragmentCompat {
  public static LicenseFragment newInstance() {
    return new LicenseFragment();
  }

  @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.license_settings, rootKey);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
