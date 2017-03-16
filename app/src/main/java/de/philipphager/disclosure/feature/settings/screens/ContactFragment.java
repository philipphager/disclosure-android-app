package de.philipphager.disclosure.feature.settings.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import de.philipphager.disclosure.R;

public class ContactFragment extends PreferenceFragmentCompat {
  public static ContactFragment newInstance() {
    return new ContactFragment();
  }

  @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.contact_settings, rootKey);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
