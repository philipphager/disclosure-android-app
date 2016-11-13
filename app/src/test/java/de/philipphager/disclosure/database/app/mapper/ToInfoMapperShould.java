package de.philipphager.disclosure.database.app.mapper;

import de.philipphager.disclosure.database.app.mocks.MockApp;
import de.philipphager.disclosure.database.app.mocks.MockPackageInfo;
import de.philipphager.disclosure.database.app.model.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class) public class ToInfoMapperShould {
  @InjectMocks ToInfoMapper toInfoMapper;

  @Test public void mapApplicationInfoToAppInfo() {
    App.Info app = toInfoMapper.map(MockPackageInfo.TEST);

    assertThat(app).isEqualTo(MockApp.TEST_INFO);
  }
}
