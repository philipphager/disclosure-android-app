package de.philipphager.disclosure.database.version;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.mocks.MockVersion;
import de.philipphager.disclosure.database.version.model.Version;
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
    Version.InsertVersion.class,
    Version.UpdateVersion.class,
})
public class VersionRepositoryShould {
  protected BriteDatabase database;
  protected VersionRepository versionRepository;
  protected Version.InsertVersion insertVersion;
  protected Version.UpdateVersion updateVersion;

  @Before public void setUp() {
    insertVersion = PowerMockito.mock(Version.InsertVersion.class);
    updateVersion = PowerMockito.mock(Version.UpdateVersion.class);
    database = PowerMockito.mock(BriteDatabase.class);
    versionRepository = new VersionRepository(insertVersion, updateVersion);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void insertVersionIntoDatabase() {
    Version version = MockVersion.TEST;
    versionRepository.insert(database, version);

    verify(insertVersion).bind(version.appId(),
        version.versionCode(),
        version.versionName(),
        version.createdAt());
    verify(database).executeInsert(insertVersion.table, insertVersion.program);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void updateVersionInDatabase() {
    Version version = MockVersion.TEST;
    versionRepository.update(database, version);

    verify(updateVersion).bind(
        version.versionName(),
        version.createdAt(),
        version.appId(),
        version.versionCode());
    verify(database).executeUpdateDelete(updateVersion.table, updateVersion.program);
  }
}
