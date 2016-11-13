package de.philipphager.disclosure.database.library.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockLibrary;
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
    LibraryApp.InsertLibraryApp.class,
})
public class LibraryAppRepositoryShould {
  protected BriteDatabase database;
  protected LibraryAppRepository libraryAppRepository;
  protected LibraryApp.InsertLibraryApp insertLibraryApp;

  @Before public void setUp() {
    insertLibraryApp = PowerMockito.mock(LibraryApp.InsertLibraryApp.class);
    database = PowerMockito.mock(BriteDatabase.class);
    libraryAppRepository = new LibraryAppRepository(insertLibraryApp);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertLibraryAppIntoDatabase() {
    libraryAppRepository.insert(database, MockLibrary.TEST.id(), MockApp.TEST.id());

    verify(insertLibraryApp).bind(MockApp.TEST.id(), MockLibrary.TEST.id());
    verify(database).executeInsert(insertLibraryApp.table, insertLibraryApp.program);
  }
}
