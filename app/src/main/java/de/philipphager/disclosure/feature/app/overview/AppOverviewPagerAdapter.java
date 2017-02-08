package de.philipphager.disclosure.feature.app.overview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.app.overview.list.AppListFragment;
import de.philipphager.disclosure.feature.app.overview.trusted.TrustedAppListFragment;
import de.philipphager.disclosure.util.ui.StringProvider;

public class AppOverviewPagerAdapter extends FragmentPagerAdapter {
  private static final int POSITION_ALL = 0;
  private static final int POSITION_TRUSTED = 1;
  private final StringProvider stringProvider;

  public AppOverviewPagerAdapter(FragmentManager fragmentManager, StringProvider stringProvider) {
    super(fragmentManager);
    this.stringProvider = stringProvider;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case POSITION_ALL:
        return AppListFragment.newInstance();
      case POSITION_TRUSTED:
        return TrustedAppListFragment.newInstance();
      default:
        throw new IllegalArgumentException("No fragment for position " + position);
    }
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case POSITION_ALL:
        return stringProvider.getString(R.string.fragment_app_overview_tabs_all);
      case POSITION_TRUSTED:
        return stringProvider.getString(R.string.fragment_app_overview_tabs_trusted);
      default:
        throw new IllegalArgumentException("No fragment for position " + position);
    }
  }
}
