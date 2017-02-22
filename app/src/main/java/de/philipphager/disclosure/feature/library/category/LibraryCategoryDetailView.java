package de.philipphager.disclosure.feature.library.category;

import android.support.annotation.StringRes;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface LibraryCategoryDetailView extends Navigates {
  void setToolbarTitle(@StringRes int title);

  void setLibraryCount(int libraryCount);

  void setUsageCount(int usageCount);

  void show(List<LibraryInfo> libraries);
}
