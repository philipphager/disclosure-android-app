package de.philipphager.disclosure.feature.library.category;

import android.support.annotation.StringRes;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import java.util.List;

public interface LibraryCategoryDetailView {
  void setToolbarTitle(@StringRes int title);

  void setLibraryCount(int libraryCount);

  void setUsageCount(int usageCount);

  void show(List<LibraryInfo> libraries);
}
