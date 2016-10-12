package de.philipphager.disclosure.database.app.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;

public class SelectAppsByNameQuery implements BriteQuery<App> {
  private final String name;

  public SelectAppsByNameQuery(String name) {
    this.name = name;
  }

  @Override public QueryObservable createQuery(BriteDatabase db) {
    return db.createQuery(App.TABLE_NAME, App.SELECTBYNAME, addWildcards(name));
  }

  @Override public RowMapper<App> rowMapper() {
    return App.FACTORY.selectByNameMapper();
  }

  private String addWildcards(final String name) {
    return String.format("%%%s%%", name);
  }
}
