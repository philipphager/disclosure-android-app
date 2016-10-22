package de.philipphager.disclosure.database.util;

import android.database.Cursor;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.app.MockApp;
import de.philipphager.disclosure.database.app.model.App;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class CursorListMapperShould {
  @Mock Cursor cursor;
  @Mock RowMapper<App> rowMapper;
  @Mock SqlBrite.Query briteQuery;
  @InjectMocks CursorToListMapper<App> cursorToListMapper;

  @Test public void returnEmptyListOnFailingCursor() {
    when(briteQuery.run()).thenReturn(null);

    List<App> result = cursorToListMapper.call(briteQuery);
    assertThat(result.size()).isEqualTo(0);
  }

  @Test public void iterateAllCursorValues() {
    when(briteQuery.run()).thenReturn(cursor);
    when(cursor.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(rowMapper.map(cursor)).thenReturn(MockApp.TEST);

    List<App> result = cursorToListMapper.call(briteQuery);
    assertThat(result.size()).isEqualTo(2);
  }
}
