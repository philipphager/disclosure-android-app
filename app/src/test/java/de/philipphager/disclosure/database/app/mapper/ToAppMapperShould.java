package de.philipphager.disclosure.database.app.mapper;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockApplicationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class ToAppMapperShould {
  @Mock protected ApplicationInfo applicationInfo;
  @Mock protected PackageManager packageManager;
  @InjectMocks protected ToAppMapper toAppMapper;

  @Test public void mapApplicationInfoToApp() {
    when(packageManager.getApplicationLabel(MockApplicationInfo.TEST2))
        .thenReturn(MockApp.TEST2.label());

    App expectedApp = App.builder()
        .label(MockApp.TEST2.label())
        .packageName(MockApp.TEST2.packageName())
        .process(MockApp.TEST2.process())
        .sourceDir(MockApp.TEST2.sourceDir())
        .flags(MockApp.TEST2.flags())
        .build();

    App app = toAppMapper.map(MockApplicationInfo.TEST2);
    assertThat(app).isEqualTo(expectedApp);
  }

  @Test(expected = IllegalStateException.class)
  public void throwErrorIfAppLabelCouldNotBeLoaded() {
    when(packageManager.getApplicationLabel(any())).thenReturn(null);

    toAppMapper.map(MockApplicationInfo.TEST2);
  }
}
