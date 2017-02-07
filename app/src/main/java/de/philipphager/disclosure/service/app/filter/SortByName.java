package de.philipphager.disclosure.service.app.filter;

import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import rx.functions.Func2;

public class SortByName implements Func2<AppWithLibraries, AppWithLibraries, Integer> {
  @Override
  public Integer call(AppWithLibraries appWithLibraries, AppWithLibraries appWithLibraries2) {
    return appWithLibraries.label().compareTo(appWithLibraries2.label());
  }
}
