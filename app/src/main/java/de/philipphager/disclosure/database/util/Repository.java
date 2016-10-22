package de.philipphager.disclosure.database.util;

import java.util.List;
import rx.Observable;

public interface Repository<T> {
  long add(T item);

  void add(Iterable<T> items);

  void update(T item);

  void remove(T item);

  Observable<List<T>> query(BriteQuery<T> briteQuery);
}
