package de.philipphager.disclosure.database.util.repository;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.util.query.SQLSelector;

public interface Editable<T> {
  long SQL_ERROR = -1;

  long add(BriteDatabase db, T item);

  int update(BriteDatabase db, T item);

  int remove(BriteDatabase db, SQLSelector s);
}
