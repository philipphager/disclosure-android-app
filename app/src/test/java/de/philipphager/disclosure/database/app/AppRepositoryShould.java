package de.philipphager.disclosure.database.app;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static de.philipphager.disclosure.database.app.MockApp.getTestContentValues;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BriteDatabase.class, SQLiteDatabase.class, BriteDatabase.Transaction.class})
public class AppRepositoryShould {
  @Mock protected BriteQuery briteQuery;
  @Mock protected DatabaseManager databaseManager;
  @InjectMocks protected AppRepository appRepository;
  protected SQLiteDatabase writeableDB;
  protected BriteDatabase readableDB;
  protected BriteDatabase.Transaction transaction;

  @Before public void setUp() {
    readableDB = PowerMockito.mock(BriteDatabase.class);
    transaction = PowerMockito.mock(BriteDatabase.Transaction.class);
    writeableDB = PowerMockito.mock(SQLiteDatabase.class);
    when(readableDB.newTransaction()).thenReturn(transaction);
    when(databaseManager.openReadable()).thenReturn(readableDB);
    when(databaseManager.openWriteable()).thenReturn(writeableDB);
    MockitoAnnotations.initMocks(this);
  }

  @Test public void insertAppIntoDatabase() {
    appRepository.add(MockApp.TEST);

    verify(writeableDB).replace(App.TABLE_NAME, null, getTestContentValues(MockApp.TEST));
  }

  @Test public void insertMultipleAppsIntoDatabase() {
    List<App> appList = Arrays.asList(MockApp.TEST, MockApp.TEST, MockApp.TEST);
    appRepository.add(appList);

    verify(readableDB, times(appList.size()))
        .insert(App.TABLE_NAME, getTestContentValues(MockApp.TEST));
    verify(transaction).markSuccessful();
  }

  @Test public void updateAppInDatabase() {
    appRepository.update(MockApp.TEST);

    String where = String.format("id=%s", MockApp.TEST.id());

    verify(readableDB).update(App.TABLE_NAME, getTestContentValues(MockApp.TEST), where);
  }

  @Test public void removeAppFromDatabase() {
    appRepository.remove(MockApp.TEST);

    String where = String.format("id=%s", MockApp.TEST.id());

    verify(readableDB).delete(App.TABLE_NAME, where);
  }
}
