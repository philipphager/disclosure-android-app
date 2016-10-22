package de.philipphager.disclosure.database.util;

import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

public interface Queryable<T> {
  Observable<List<T>> query(BriteDatabase db, BriteQuery<T> briteQuery);
}
