package de.philipphager.disclosure.feature.library.category;

import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.feature.preference.ui.OnlyShowUsedLibraries;
import javax.inject.Inject;

public class LibraryCategoryPresenter {
  private final Preference<Boolean> onlyShowUsedLibraries;

  @Inject public LibraryCategoryPresenter(@OnlyShowUsedLibraries Preference<Boolean> onlyShowUsedLibraries) {
    this.onlyShowUsedLibraries = onlyShowUsedLibraries;
  }

  public void onShowOnlyUsedLibrariesClicked() {
    onlyShowUsedLibraries.set(!onlyShowUsedLibraries.get());
  }
}
