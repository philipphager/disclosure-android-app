package de.philipphager.disclosure.database.util;

import android.database.sqlite.SQLiteDatabase;

public interface Editable<T> {
  long add(SQLiteDatabase db, T item);

  void add(SQLiteDatabase db, Iterable<T> items);

  void update(SQLiteDatabase db, T item);

  void remove(SQLiteDatabase db, SQLQuery query);
}
