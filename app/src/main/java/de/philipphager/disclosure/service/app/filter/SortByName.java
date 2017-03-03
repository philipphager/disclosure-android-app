package de.philipphager.disclosure.service.app.filter;

import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import rx.functions.Func2;

public class SortByName implements Func2<AppReport, AppReport, Integer> {
  @Override
  public Integer call(AppReport appReport, AppReport appReport2) {
    return appReport.App().label().compareTo(appReport2.App().label());
  }
}
