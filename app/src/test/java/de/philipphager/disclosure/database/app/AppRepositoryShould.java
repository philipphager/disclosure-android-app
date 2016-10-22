package de.philipphager.disclosure.database.app;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
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
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@PrepareForTest({BriteDatabase.class, SQLiteDatabase.class, BriteDatabase.Transaction.class})
public class AppRepositoryShould {
  @Mock protected BriteQuery briteQuery;
  @InjectMocks protected AppRepository appRepository;
  protected SQLiteDatabase writeableDB;
  protected BriteDatabase observableDB;
  protected BriteDatabase.Transaction transaction;

  @Before public void setUp() {
    observableDB = PowerMockito.mock(BriteDatabase.class);
    transaction = PowerMockito.mock(BriteDatabase.Transaction.class);
    writeableDB = PowerMockito.mock(SQLiteDatabase.class);
    when(observableDB.newTransaction()).thenReturn(transaction);
    MockitoAnnotations.initMocks(this);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertAppIntoDatabase() {
    appRepository.add(writeableDB, MockApp.TEST);

    verify(writeableDB).replace(App.TABLE_NAME, null, getTestContentValues(MockApp.TEST));
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertMultipleAppsIntoDatabase() {
    List<App> appList = Arrays.asList(MockApp.TEST, MockApp.TEST, MockApp.TEST);
    appRepository.add(writeableDB, appList);

    verify(writeableDB, times(appList.size()))
        .insert(App.TABLE_NAME, null,  getTestContentValues(MockApp.TEST));
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void updateAppInDatabase() {
    appRepository.update(writeableDB, MockApp.TEST);

    String where = String.format("id=%s", MockApp.TEST.id());

    verify(writeableDB).update(App.TABLE_NAME,  getTestContentValues(MockApp.TEST), where, null);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void removeAppFromDatabase() {
    appRepository.remove(writeableDB, MockApp.TEST);

    String where = String.format("id=%s", MockApp.TEST.id());

    verify(writeableDB).delete(App.TABLE_NAME, where, null);
  }
}
