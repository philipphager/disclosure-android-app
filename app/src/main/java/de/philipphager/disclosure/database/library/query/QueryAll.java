package de.philipphager.disclosure.database.library.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.util.query.BriteQuery;

public class QueryAll implements BriteQuery<Library> {
  @Override public QueryObservable create(BriteDatabase db) {
    return db.createQuery(Library.TABLE_NAME, Library.SELECTALL);
  }

  @Override public RowMapper<Library> rowMapper() {
    return Library.FACTORY.selectAllMapper();
  }
}
