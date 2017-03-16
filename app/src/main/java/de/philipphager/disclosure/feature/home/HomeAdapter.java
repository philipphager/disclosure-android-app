package de.philipphager.disclosure.feature.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.philipphager.disclosure.feature.app.manager.AppManagerFragment;
import de.philipphager.disclosure.feature.library.category.LibraryCategoryFragment;
import de.philipphager.disclosure.feature.settings.SettingsFragment;

public class HomeAdapter extends FragmentPagerAdapter {
  private static final int POSITION_APP_OVERVIEW = 0;
  private static final int POSITION_LIBRARY_OVERVIEW = 1;
  private static final int POSITION_SETTINGS = 2;

  public HomeAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case POSITION_APP_OVERVIEW:
        return AppManagerFragment.newInstance();
      case POSITION_LIBRARY_OVERVIEW       :
        return LibraryCategoryFragment.newInstance();
      case POSITION_SETTINGS:
        return SettingsFragment.newInstance();
      default:
        throw new IllegalArgumentException(
            String.format("Out of bounds! HomeAdapter only manages %s fragments", getCount()));
    }
  }

  @Override public int getCount() {
    return 3;
  }
}
