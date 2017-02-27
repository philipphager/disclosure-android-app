package de.philipphager.disclosure.feature.library.category.usecase;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.LibraryService;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func2;

public class FetchLibraryCategories {
  private final LibraryService libraryService;

  @Inject public FetchLibraryCategories(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  public Observable<List<LibraryCategory>> run() {
    return allCategories()
        .flatMap(type -> Observable.combineLatest(libraryService.countByType(type),
            libraryService.countUsedLibrariesByType(type),
            (allLibraries, usedLibraries) ->
                LibraryCategory.create(type, allLibraries, usedLibraries)).first()
        ).toSortedList(new SortByName());
  }

  private Observable<Library.Type> allCategories() {
    return Observable.from(Arrays.asList(Library.Type.ADVERTISEMENT,
        Library.Type.SOCIAL,
        Library.Type.ANALYTICS,
        Library.Type.DEVELOPER));
  }

  private static class SortByName implements Func2<LibraryCategory, LibraryCategory, Integer> {
    @Override public Integer call(LibraryCategory category, LibraryCategory category2) {
      return category.type().compareTo(category2.type());
    }
  }
}
