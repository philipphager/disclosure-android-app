package de.philipphager.disclosure.database.util;

import android.database.Cursor;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.RowMapper;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func1;

public class CursorListMapper<T> implements Func1<SqlBrite.Query, List<T>> {
  private final RowMapper<T> rowMapper;

  public CursorListMapper(RowMapper<T> rowMapper) {
    this.rowMapper = rowMapper;
  }

  @Override public List<T> call(SqlBrite.Query query) {
    Cursor cursor = query.run();

    boolean cursorPresent = cursor != null;
    int resultSize = cursorPresent ? cursor.getColumnCount() : 0;
    List<T> items = new ArrayList<>(resultSize);

    if (cursorPresent) {
      while (cursor.moveToNext()) {
        items.add(rowMapper.map(cursor));
      }
    }

    return items;
  }
}
