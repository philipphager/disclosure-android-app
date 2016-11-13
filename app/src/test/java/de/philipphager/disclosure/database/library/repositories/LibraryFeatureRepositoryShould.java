package de.philipphager.disclosure.database.library.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import de.philipphager.disclosure.database.mocks.MockLibraryFeature;
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
    LibraryFeature.InsertLibraryFeature.class,
    LibraryFeature.UpdateLibraryFeature.class
})
public class LibraryFeatureRepositoryShould {
  protected BriteDatabase database;
  protected LibraryFeatureRepository libraryFeatureRepository;
  protected LibraryFeature.InsertLibraryFeature insertLibraryFeature;
  protected LibraryFeature.UpdateLibraryFeature updateLibraryFeature;

  @Before public void setUp() {
    insertLibraryFeature = PowerMockito.mock(LibraryFeature.InsertLibraryFeature.class);
    updateLibraryFeature = PowerMockito.mock(LibraryFeature.UpdateLibraryFeature.class);
    database = PowerMockito.mock(BriteDatabase.class);
    libraryFeatureRepository =
        new LibraryFeatureRepository(insertLibraryFeature, updateLibraryFeature);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertLibraryFeatureIntoDatabase() {
    LibraryFeature libraryFeature = MockLibraryFeature.TEST;
    libraryFeatureRepository.insert(database, libraryFeature);

    verify(insertLibraryFeature).bind(libraryFeature.id(),
        libraryFeature.libraryId(),
        libraryFeature.featureId(),
        libraryFeature.createdAt(),
        libraryFeature.updatedAt());
    verify(database).executeInsert(insertLibraryFeature.table, insertLibraryFeature.program);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void updateLibraryFeatureInDatabase() {
    LibraryFeature libraryFeature = MockLibraryFeature.TEST;
    libraryFeatureRepository.update(database, libraryFeature);

    verify(updateLibraryFeature).bind(libraryFeature.libraryId(),
        libraryFeature.featureId(),
        libraryFeature.createdAt(),
        libraryFeature.updatedAt(),
        libraryFeature.id());
    verify(database).executeUpdateDelete(updateLibraryFeature.table, updateLibraryFeature.program);
  }
}
