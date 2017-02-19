package de.philipphager.disclosure.feature.library.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.util.ui.BaseFragment;

public class LibraryListFragment extends BaseFragment {
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override protected int getLayout() {
    return 0;
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {

  }
}
