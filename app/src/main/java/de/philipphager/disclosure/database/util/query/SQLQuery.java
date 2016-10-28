package de.philipphager.disclosure.database.util.query;

public interface SQLQuery<T> extends Query<T> {
  String create();
}
