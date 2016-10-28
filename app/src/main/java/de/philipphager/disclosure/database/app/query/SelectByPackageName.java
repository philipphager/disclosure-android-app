package de.philipphager.disclosure.database.app.query;

import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.query.SQLSelector;

public class SelectByPackageName implements SQLSelector {
  private final String packageName;

  public SelectByPackageName(String packageName) {
    this.packageName = packageName;
  }

  @Override public String create() {
    return String.format("%s = '%s'", App.PACKAGENAME, packageName);
  }
}
