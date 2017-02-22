package de.philipphager.disclosure.feature.library.overview;

import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import java.util.List;

public interface LibraryCategoryView {
  void show(List<LibraryCategory> libraryCategories);
}
