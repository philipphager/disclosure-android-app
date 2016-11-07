package de.philipphager.disclosure.feature.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.philipphager.disclosure.feature.app.overview.AppOverviewFragment;

public class HomeAdapter extends FragmentPagerAdapter {
  private static final int POSITION_APP_OVERVIEW = 0;

  public HomeAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case POSITION_APP_OVERVIEW:
        return AppOverviewFragment.newInstance();
      default:
        throw new IllegalArgumentException(
            String.format("Out of bounds! HomeAdapter only manages %s fragments", getCount()));
    }
  }

  @Override public int getCount() {
    return 1;
  }
}
