package de.philipphager.disclosure.util.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.DisclosureApp;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.feature.navigation.Navigator;
import javax.inject.Inject;

public abstract class BaseDialogFragment extends AppCompatDialogFragment implements Navigates {
  @Inject protected Navigator navigator;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inject();
  }

  @Override public Navigator navigate() {
    return navigator;
  }

  protected abstract void injectFragment(ApplicationComponent appComponent);

  private void inject() {
    DisclosureApp application = (DisclosureApp) getActivity().getApplication();
    injectFragment(application.getApplicationComponent());

    navigator.setActivity(getActivity());
  }
}
