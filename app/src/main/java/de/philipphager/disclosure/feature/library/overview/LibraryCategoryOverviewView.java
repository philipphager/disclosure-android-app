package de.philipphager.disclosure.feature.library.overview;

import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import de.philipphager.disclosure.feature.navigation.Navigates;
import java.util.List;

public interface LibraryCategoryOverviewView extends Navigates {
  void show(List<LibraryCategory> libraryCategories);
}
