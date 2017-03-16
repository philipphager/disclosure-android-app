package de.philipphager.disclosure.service.app.filter;

import de.philipphager.disclosure.database.app.model.AppReport;
import rx.functions.Func2;

/**
 * Sort apps by their analysed permission count. If both apps weren't analyzed
 * yet, sort them by library count.
 */
public class SortByPermissionCount implements Func2<AppReport, AppReport, Integer> {
  @Override
  public Integer call(AppReport appReport, AppReport otherAppReport) {
    if (appReport.permissionCount() > 0 || otherAppReport.permissionCount() > 0) {
      return (int) (otherAppReport.permissionCount() - appReport.permissionCount());
    } else {
      return (int) (otherAppReport.libraryCount() - appReport.libraryCount());
    }
  }
}
