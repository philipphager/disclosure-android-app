package de.philipphager.disclosure.service.app.filter;

import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import rx.functions.Func2;

/**
 * Sort apps by the last analyzed date. If both apps weren't analyzed yet,
 * sort them by library count.
 */
public class SortByAnalyzedAt implements Func2<AppWithLibraries, AppWithLibraries, Integer> {

  @Override public Integer call(AppWithLibraries app, AppWithLibraries otherApp) {
    if (app.analyzedAt() != null && otherApp.analyzedAt() != null) {
      return otherApp.analyzedAt().compareTo(app.analyzedAt());
    } else if (app.analyzedAt() != null && otherApp.analyzedAt() == null) {
      return -1;
    } else if (app.analyzedAt() == null && otherApp.analyzedAt() != null) {
      return 1;
    }

    return (int) (otherApp.libraryCount() - app.libraryCount());
  }
}
