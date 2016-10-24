package de.philipphager.disclosure.feature.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseFragment;

public class AppOverviewFragment extends BaseFragment {
  public static AppOverviewFragment newInstance() {
    return new AppOverviewFragment();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override protected int getLayout() {
    return R.layout.fragment_overview;
  }
}
