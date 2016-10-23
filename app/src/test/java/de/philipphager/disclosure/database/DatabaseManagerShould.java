package de.philipphager.disclosure.database;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("PMD.TooManyStaticImports")
@RunWith(PowerMockRunner.class)
@PrepareForTest({SqlBrite.class, BriteDatabase.class, SQLiteDatabase.class})
public class DatabaseManagerShould {
  @Mock protected DatabaseOpenHelper openHelper;
  private SqlBrite sqlBrite;
  private BriteDatabase observableDB;
  private BriteDatabase secondObservableDB;
  private SQLiteDatabase writeableDB;
  private SQLiteDatabase secondWritableDB;
  private DatabaseManager databaseManager;

  @Before public void setUp() {
    sqlBrite = PowerMockito.mock(SqlBrite.class);
    observableDB = PowerMockito.mock(BriteDatabase.class);
    secondObservableDB = PowerMockito.mock(BriteDatabase.class);
    writeableDB = PowerMockito.mock(SQLiteDatabase.class);
    secondWritableDB = PowerMockito.mock(SQLiteDatabase.class);

    MockitoAnnotations.initMocks(this);
    databaseManager = new DatabaseManager(openHelper, sqlBrite);

    when(openHelper.getWritableDatabase()).thenReturn(writeableDB).thenReturn(secondWritableDB);
    when(sqlBrite.wrapDatabaseHelper(any(DatabaseOpenHelper.class),
        any(Scheduler.class))).thenReturn(observableDB).thenReturn(secondObservableDB);
  }

  @Test public void openNewWritableDBOnFirstOpenCall() {
    SQLiteDatabase db = databaseManager.openWriteable();

    verify(openHelper).getWritableDatabase();
    assertThat(db).isEqualTo(writeableDB);
  }

  @Test public void reuseWritableDBWhenDBAlreadyOpened() {
    SQLiteDatabase firstDB = databaseManager.openWriteable();
    SQLiteDatabase secondDB = databaseManager.openWriteable();

    verify(openHelper, times(1)).getWritableDatabase();
    assertThat(firstDB).isEqualTo(secondDB);
  }

  @Test public void openNewObservableDBOnFirstOpenCall() {
    BriteDatabase db = databaseManager.openObservable();

    verify(sqlBrite.wrapDatabaseHelper(any(DatabaseOpenHelper.class), any(Scheduler.class)));
    assertThat(db).isEqualTo(observableDB);
  }

  @Test public void reuseObservableDBWhenDBAlreadyOpened() {
    BriteDatabase firstDB = databaseManager.openObservable();
    BriteDatabase secondDB = databaseManager.openObservable();

    verify(sqlBrite.wrapDatabaseHelper(any(DatabaseOpenHelper.class), any(Scheduler.class)));
    assertThat(firstDB).isEqualTo(secondDB);
  }
}
