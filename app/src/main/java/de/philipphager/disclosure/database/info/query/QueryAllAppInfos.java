package de.philipphager.disclosure.database.info.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.query.BriteQuery;

public class QueryAllAppInfos implements BriteQuery<App.Info> {
  @Override public QueryObservable create(BriteDatabase db) {
    return db.createQuery(App.TABLE_NAME, App.SELECTALLINFOS);
  }

  @Override public RowMapper<App.Info> rowMapper() {
    return App.Info.MAPPER;
  }
}
