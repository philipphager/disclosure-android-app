package de.philipphager.disclosure.database.app.query;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.SQLQuery;

public class DeleteAppsByPackageName implements SQLQuery {
  private final String packageName;

  public DeleteAppsByPackageName(String packageName) {
    this.packageName = packageName;
  }

  @Override public String toSQL() {
    return String.format("%s = '%s'", App.PACKAGENAME, packageName);
  }
}
