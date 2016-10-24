package de.philipphager.disclosure.database;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("PMD.TooManyStaticImports")
@RunWith(PowerMockRunner.class)
@PrepareForTest({SqlBrite.class, BriteDatabase.class})
public class DatabaseManagerShould {
  @Mock protected DatabaseOpenHelper openHelper;
  private SqlBrite sqlBrite;
  private BriteDatabase observableDB;
  private DatabaseManager databaseManager;

  @Before public void setUp() {
    sqlBrite = PowerMockito.mock(SqlBrite.class);
    observableDB = PowerMockito.mock(BriteDatabase.class);

    MockitoAnnotations.initMocks(this);
    databaseManager = new DatabaseManager(openHelper, sqlBrite);

    when(sqlBrite.wrapDatabaseHelper(any(DatabaseOpenHelper.class),
        any(Scheduler.class))).thenReturn(observableDB);
  }

  @Test public void openNewDBOnFirstCall() {
    BriteDatabase db = databaseManager.get();

    verify(sqlBrite.wrapDatabaseHelper(any(DatabaseOpenHelper.class), any(Scheduler.class)));
    assertThat(db).isEqualTo(observableDB);
  }

  @Test public void reuseDBWhenDBAlreadyOpened() {
    BriteDatabase firstDB = databaseManager.get();
    BriteDatabase secondDB = databaseManager.get();

    verify(sqlBrite.wrapDatabaseHelper(any(DatabaseOpenHelper.class), any(Scheduler.class)));
    assertThat(firstDB).isEqualTo(secondDB);
  }
}
