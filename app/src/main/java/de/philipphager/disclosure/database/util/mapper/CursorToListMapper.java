package de.philipphager.disclosure.database.util.mapper;

import android.database.Cursor;
import com.squareup.sqldelight.RowMapper;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func1;

public class CursorToListMapper<T> implements Func1<Cursor, List<T>> {
  private final RowMapper<T> rowMapper;

  public CursorToListMapper(RowMapper<T> rowMapper) {
    this.rowMapper = rowMapper;
  }

  @Override public List<T> call(Cursor cursor) {
    boolean cursorPresent = cursor != null;
    int resultSize = cursorPresent ? cursor.getCount() : 0;
    List<T> items = new ArrayList<>(resultSize);

    if (cursorPresent) {
      cursor.moveToFirst();

      while (cursor.moveToNext()) {
        items.add(rowMapper.map(cursor));
      }
    }
    return items;
  }
}
