package de.philipphager.disclosure.service.app.filter;

import de.philipphager.disclosure.database.app.model.AppReport;
import rx.functions.Func2;

/**
 * Sort apps by the last analyzed date. If both apps weren't analyzed yet,
 * sort them by library count.
 */
public class SortByAnalyzedAt implements Func2<AppReport, AppReport, Integer> {

  @Override public Integer call(AppReport appReport, AppReport otherAppReport) {
    if (appReport.App().analyzedAt() != null && otherAppReport.App().analyzedAt() != null) {
      return otherAppReport.App().analyzedAt().compareTo(appReport.App().analyzedAt());
    } else if (appReport.App().analyzedAt() != null && otherAppReport.App().analyzedAt() == null) {
      return -1;
    } else if (appReport.App().analyzedAt() == null && otherAppReport.App().analyzedAt() != null) {
      return 1;
    }

    return (int) (otherAppReport.libraryCount() - appReport.libraryCount());
  }
}
