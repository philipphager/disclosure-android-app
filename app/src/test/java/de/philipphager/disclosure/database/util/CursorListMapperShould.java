package de.philipphager.disclosure.database.util;

import android.database.Cursor;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.app.mocks.MockApp;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class CursorListMapperShould {
  @Mock protected Cursor cursor;
  @Mock protected RowMapper<App> rowMapper;
  @InjectMocks protected CursorToListMapper<App> cursorToListMapper;

  @Test public void returnEmptyListOnFailingCursor() {
    when(cursor.moveToNext()).thenReturn(false);

    List<App> result = cursorToListMapper.call(cursor);
    assertThat(result.size()).isEqualTo(0);
  }

  @Test public void iterateAllCursorValues() {
    when(cursor.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(rowMapper.map(cursor)).thenReturn(MockApp.TEST);

    List<App> result = cursorToListMapper.call(cursor);
    assertThat(result.size()).isEqualTo(2);
  }
}
