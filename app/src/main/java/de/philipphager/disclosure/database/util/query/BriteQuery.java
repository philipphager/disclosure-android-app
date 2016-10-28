package de.philipphager.disclosure.database.util.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

public interface BriteQuery<T> extends Query<T> {
  QueryObservable create(BriteDatabase db);
}
