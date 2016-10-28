package de.philipphager.disclosure.database.util.repository;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.util.query.BriteQuery;
import de.philipphager.disclosure.database.util.query.SQLQuery;
import java.util.List;
import rx.Observable;

public interface Queryable<T> {
  List<T> query(BriteDatabase db, SQLQuery<T> query);

  Observable<List<T>> query(BriteDatabase db, BriteQuery<T> query);
}
