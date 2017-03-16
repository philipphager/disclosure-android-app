package de.philipphager.disclosure.feature.settings.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.R;

public class LicenseFragment extends PreferenceFragmentCompat {
  @BindView(R.id.toolbar) Toolbar toolbar;

  public static LicenseFragment newInstance() {
    return new LicenseFragment();
  }

  @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.license_settings, rootKey);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ButterKnife.bind(this, view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);
    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    activity.getSupportActionBar().setTitle(R.string.fragment_license_title);
  }
}
