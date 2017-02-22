package de.philipphager.disclosure.feature.library.overview;

import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import javax.inject.Inject;

public class LibraryCategoryDetailPresenter {
  private LibraryCategory libraryCategory;

  @Inject public LibraryCategoryDetailPresenter() {
  }

  public void onCreate(LibraryCategory libraryCategory) {
    this.libraryCategory = libraryCategory;
  }
}
