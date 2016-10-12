package de.philipphager.disclosure.database.util;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqldelight.RowMapper;

public interface BriteQuery<T> {
  QueryObservable createQuery(BriteDatabase db);

  RowMapper<T> rowMapper();
}
