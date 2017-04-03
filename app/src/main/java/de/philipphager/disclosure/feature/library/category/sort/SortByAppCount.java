package de.philipphager.disclosure.feature.library.category.sort;

import de.philipphager.disclosure.database.library.model.LibraryInfo;
import rx.functions.Func2;

public class SortByAppCount implements Func2<LibraryInfo, LibraryInfo, Integer> {
  @Override
  public Integer call(LibraryInfo libraryInfo, LibraryInfo libraryInfo2) {
    return (int) (libraryInfo2.appCount() - libraryInfo.appCount());
  }
}
