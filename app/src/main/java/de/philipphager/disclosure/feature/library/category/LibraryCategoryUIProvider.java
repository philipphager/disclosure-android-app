package de.philipphager.disclosure.feature.library.category;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;

public final class LibraryCategoryUIProvider {
  public static @StringRes int getTitle(Library.Type type) {
    switch (type) {
      case ADVERTISMENT:
        return R.string.library_category_advertisement;
      case ANALYTICS:
        return R.string.library_category_analytics;
      case DEVELOPER:
        return R.string.library_category_development;
      case SOCIAL:
        return R.string.library_category_social;
      default:
        throw new IllegalArgumentException("No title for library category");
    }
  }

  public static @DrawableRes int getIcon(Library.Type type) {
    switch (type) {
      case ADVERTISMENT:
        return R.drawable.ic_android;
      case ANALYTICS:
        return R.drawable.ic_android;
      case DEVELOPER:
        return R.drawable.ic_android;
      case SOCIAL:
        return R.drawable.ic_android;
      default:
        throw new IllegalArgumentException("No icon for library category");
    }
  }
}
