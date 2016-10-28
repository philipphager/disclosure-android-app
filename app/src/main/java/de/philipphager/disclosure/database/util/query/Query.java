package de.philipphager.disclosure.database.util.query;

import com.squareup.sqldelight.RowMapper;

public interface Query<T> {
  RowMapper<T> rowMapper();
}
