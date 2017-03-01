package de.philipphager.disclosure.feature.library.category;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.library.category.list.LibraryListFragment;
import de.philipphager.disclosure.util.ui.StringProvider;

public class LibraryCategoryPagerAdapter extends FragmentPagerAdapter {
  private static final int POSITION_ADVERTISEMENT = 0;
  private static final int POSITION_ANALYTICS = 1;
  private static final int POSITION_SOCIAL = 2;
  private static final int POSITION_DEVELOPER = 3;
  private final StringProvider stringProvider;

  public LibraryCategoryPagerAdapter(FragmentManager fragmentManager, StringProvider stringProvider) {
    super(fragmentManager);
    this.stringProvider = stringProvider;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case POSITION_ADVERTISEMENT:
        return LibraryListFragment.newInstance(Library.Type.ADVERTISEMENT);
      case POSITION_ANALYTICS:
        return LibraryListFragment.newInstance(Library.Type.ANALYTICS);
      case POSITION_SOCIAL:
        return LibraryListFragment.newInstance(Library.Type.SOCIAL);
      case POSITION_DEVELOPER:
        return LibraryListFragment.newInstance(Library.Type.DEVELOPER);
      default:
        throw new IllegalArgumentException("No fragment for position " + position);
    }
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case POSITION_ADVERTISEMENT:
        return stringProvider.getString(R.string.library_category_advertisement);
      case POSITION_ANALYTICS:
        return stringProvider.getString(R.string.library_category_analytics);
      case POSITION_SOCIAL:
        return stringProvider.getString(R.string.library_category_social);
      case POSITION_DEVELOPER:
        return stringProvider.getString(R.string.library_category_development);
      default:
        throw new IllegalArgumentException("No fragment for position " + position);
    }
  }

  @Override public int getCount() {
    return 4;
  }
}
