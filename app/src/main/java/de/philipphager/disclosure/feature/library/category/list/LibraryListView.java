package de.philipphager.disclosure.feature.library.category.list;

import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface LibraryListView extends Navigates {
  void setLibraryCount(int libraryCount);

  void show(List<LibraryInfo> libraries);
}
