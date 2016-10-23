package de.philipphager.disclosure.database.version.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.version.model.Version;

public class SelectAllVersionsQuery implements BriteQuery<Version> {
  @Override public QueryObservable createQuery(BriteDatabase db) {
    return db.createQuery(Version.TABLE_NAME, Version.SELECTALL);
  }

  @Override public RowMapper<Version> rowMapper() {
    return Version.FACTORY.selectAllMapper();
  }
}
