package de.philipphager.disclosure.database.version.mapper;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockPackageInfo;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.util.time.Clock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class ToVersionMapperShould {
  @Mock Clock clock;

  @Test public void mapPackageInfoToVersion() {
    LocalDateTime mockNow = LocalDateTime.of(2016, 1, 1, 0, 0);
    PackageInfo packageInfo = MockPackageInfo.TEST;
    App app = MockApp.TEST;

    when(clock.now()).thenReturn(mockNow);

    ToVersionMapper mapper = new ToVersionMapper(clock, app.id());
    Version expectedVersion = Version.create(app.id(),
        packageInfo.versionCode,
        packageInfo.versionName,
        mockNow);

    Version version = mapper.map(MockPackageInfo.TEST);
    assertThat(version).isEqualTo(expectedVersion);
  }
}
