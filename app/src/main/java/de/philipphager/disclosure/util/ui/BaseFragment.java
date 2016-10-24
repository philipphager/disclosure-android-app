package de.philipphager.disclosure.util.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.DisclosureApp;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.feature.navigation.Navigator;
import javax.inject.Inject;

public abstract class BaseFragment extends Fragment implements Navigates {
  @Inject protected Navigator navigator;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inject();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(getLayout(), container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public Navigator navigate() {
    return navigator;
  }

  protected abstract int getLayout();

  protected abstract void injectFragment(ApplicationComponent appComponent);

  private void inject() {
    DisclosureApp application = (DisclosureApp) getActivity().getApplication();
    injectFragment(application.getApplicationComponent());

    navigator.setActivity(getActivity());
  }
}
