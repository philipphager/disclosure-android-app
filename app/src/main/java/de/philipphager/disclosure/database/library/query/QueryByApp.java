package de.philipphager.disclosure.database.library.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.util.query.BriteQuery;

public class QueryByApp implements BriteQuery<Library> {
  private final App app;

  public QueryByApp(App app) {
    this.app = app;
  }

  @Override public QueryObservable create(BriteDatabase db) {
    String appId = String.valueOf(app.id());
    return db.createQuery(Library.TABLE_NAME, Library.SELECTBYAPP, appId);
  }

  @Override public RowMapper<Library> rowMapper() {
    return Library.FACTORY.selectByAppMapper();
  }
}
