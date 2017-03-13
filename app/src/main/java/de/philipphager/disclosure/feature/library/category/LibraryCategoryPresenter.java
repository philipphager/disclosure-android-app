package de.philipphager.disclosure.feature.library.category;

import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.feature.preference.ui.OnlyShowUsedLibraries;
import javax.inject.Inject;

public class LibraryCategoryPresenter {
  private final Preference<Boolean> onlyShowUsedLibraries;
  private LibraryCategoryView view;

  @Inject public LibraryCategoryPresenter(@OnlyShowUsedLibraries Preference<Boolean> onlyShowUsedLibraries) {
    this.onlyShowUsedLibraries = onlyShowUsedLibraries;
  }

  public void onCreate(LibraryCategoryView view) {
    this.view = view;
  }

  public void onShowOnlyUsedLibrariesClicked() {
    onlyShowUsedLibraries.set(!onlyShowUsedLibraries.get());
  }

  public void onAddLibraryClicked() {
    view.navigate().toCreateLibrary();
  }
}
