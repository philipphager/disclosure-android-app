package de.philipphager.disclosure.database.version.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.util.query.BriteQuery;
import de.philipphager.disclosure.database.version.model.Version;

public class QueryVersionByApp implements BriteQuery<Version> {
  private final long appId;

  public QueryVersionByApp(long appId) {
    this.appId = appId;
  }

  @Override public QueryObservable create(BriteDatabase db) {
    return db.createQuery(Version.TABLE_NAME, Version.SELECTBYAPP, String.valueOf(appId));
  }

  @Override public RowMapper<Version> rowMapper() {
    return Version.FACTORY.selectByAppMapper();
  }
}
