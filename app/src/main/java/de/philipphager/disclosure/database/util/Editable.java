package de.philipphager.disclosure.database.util;

import com.squareup.sqlbrite.BriteDatabase;

public interface Editable<T> {
  long add(BriteDatabase db, T item);

  void add(BriteDatabase db, Iterable<T> items);

  void update(BriteDatabase db, T item);

  void remove(BriteDatabase db, SQLQuery query);
}
