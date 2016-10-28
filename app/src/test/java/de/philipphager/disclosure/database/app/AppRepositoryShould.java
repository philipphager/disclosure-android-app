package de.philipphager.disclosure.database.app;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.query.BriteQuery;
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
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@PrepareForTest({BriteDatabase.class, BriteDatabase.Transaction.class})
public class AppRepositoryShould {
  @Mock protected BriteQuery briteQuery;
  @InjectMocks protected AppRepository appRepository;
  protected BriteDatabase database;
  protected BriteDatabase.Transaction transaction;

  @Before public void setUp() {
    database = PowerMockito.mock(BriteDatabase.class);
    transaction = PowerMockito.mock(BriteDatabase.Transaction.class);
    when(database.newTransaction()).thenReturn(transaction);
    MockitoAnnotations.initMocks(this);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertAppIntoDatabase() {
    appRepository.add(database, MockApp.TEST);

    verify(database).insert(App.TABLE_NAME, getTestContentValues(MockApp.TEST),
        SQLiteDatabase.CONFLICT_IGNORE);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertMultipleAppsIntoDatabase() {
    List<App> appList = Arrays.asList(MockApp.TEST, MockApp.TEST, MockApp.TEST);
    appRepository.add(database, appList);

    verify(database, times(3)).insert(App.TABLE_NAME, getTestContentValues(MockApp.TEST),
        SQLiteDatabase.CONFLICT_IGNORE);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void updateAppInDatabase() {
    appRepository.update(database, MockApp.TEST);

    String where = String.format("id=%s", MockApp.TEST.id());

    verify(database).update(App.TABLE_NAME, getTestContentValues(MockApp.TEST), where);
  }
}
