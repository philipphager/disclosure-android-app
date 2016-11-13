package de.philipphager.disclosure.database.app;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.mocks.MockApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@PrepareForTest({
    BriteDatabase.class,
    App.InsertApp.class,
    App.UpdateApp.class
})
public class AppRepositoryShould {
  protected AppRepository appRepository;
  protected BriteDatabase database;
  protected App.InsertApp insertApp;
  protected App.UpdateApp updateApp;

  @Before public void setUp() {
    insertApp = PowerMockito.mock(App.InsertApp.class);
    updateApp = PowerMockito.mock(App.UpdateApp.class);
    database = PowerMockito.mock(BriteDatabase.class);
    appRepository = new AppRepository(insertApp, updateApp);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertAppIntoDatabase() {
    App app = MockApp.TEST;
    appRepository.insert(database, app);

    verify(insertApp)
        .bind(app.label(), app.packageName(), app.process(), app.sourceDir(), app.flags());
    verify(database).executeInsert(App.TABLE_NAME, insertApp.program);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void updateAppInDatabase() {
    App app = MockApp.TEST;
    appRepository.update(database, app);

    verify(updateApp)
        .bind(app.label(), app.process(), app.sourceDir(), app.flags(), app.packageName());
    verify(database).executeUpdateDelete(App.TABLE_NAME, updateApp.program);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void deleteAppFromDatabase() {
    String testWhere = "id = 1";
    appRepository.delete(database, testWhere);

    verify(database).delete(App.TABLE_NAME, testWhere);
  }
}
