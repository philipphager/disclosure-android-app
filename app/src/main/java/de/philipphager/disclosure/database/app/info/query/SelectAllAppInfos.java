package de.philipphager.disclosure.database.app.info.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;

public class SelectAllAppInfos implements BriteQuery<App.Info> {
  @Override public QueryObservable createQuery(BriteDatabase db) {
    return db.createQuery(App.TABLE_NAME, App.SELECTALLINFOS);
  }

  @Override public RowMapper<App.Info> rowMapper() {
    return App.Info.MAPPER;
  }
}
