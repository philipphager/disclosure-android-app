package de.philipphager.disclosure.database.app;

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
@PrepareForTest({ BriteDatabase.class, BriteDatabase.Transaction.class })
public class AppRepositoryShould {
  @Mock protected BriteQuery briteQuery;
  @Mock protected DatabaseManager databaseManager;
  @InjectMocks protected AppRepository appRepository;
  protected BriteDatabase database;
  protected BriteDatabase.Transaction transaction;

  @Before public void setUp() {
    database = PowerMockito.mock(BriteDatabase.class);
    transaction = PowerMockito.mock(BriteDatabase.Transaction.class);
    when(database.newTransaction()).thenReturn(transaction);
    when(databaseManager.open()).thenReturn(database);
    MockitoAnnotations.initMocks(this);
  }

  @Test public void insertAppIntoDatabase() {
    appRepository.add(MockApp.TEST_APP);

    verify(database).insert(App.TABLE_NAME, getTestContentValues(MockApp.TEST_APP));
    verify(database).close();
  }

  @Test public void insertMultipleAppsIntoDatabase() {
    List<App> appList = Arrays.asList(MockApp.TEST_APP, MockApp.TEST_APP, MockApp.TEST_APP);
    appRepository.add(appList);

    verify(database, times(appList.size())).insert(App.TABLE_NAME,
        getTestContentValues(MockApp.TEST_APP));
    verify(transaction).markSuccessful();
    verify(database).close();
  }

  @Test public void updateAppInDatabase() {
    appRepository.update(MockApp.TEST_APP);

    String where = String.format("id=%s", MockApp.TEST_APP.id());

    verify(database).update(App.TABLE_NAME, getTestContentValues(MockApp.TEST_APP), where);
    verify(database).close();
  }

  @Test public void removeAppFromDatabase() {
    appRepository.remove(MockApp.TEST_APP);

    String where = String.format("id=%s", MockApp.TEST_APP.id());

    verify(database).delete(App.TABLE_NAME, where);
    verify(database).close();
  }
}
