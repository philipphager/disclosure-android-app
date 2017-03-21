package de.philipphager.disclosure.service.filter;

import de.philipphager.disclosure.database.app.model.AppReport;
import rx.functions.Func2;

public class SortByLibraryCount implements Func2<AppReport, AppReport, Integer> {
  @Override
  public Integer call(AppReport appReport, AppReport appReport2) {
    return (int) (appReport2.libraryCount() - appReport.libraryCount());
  }
}
