package de.philipphager.disclosure.database.app.mapper;

import de.philipphager.disclosure.database.app.model.AppInfo;
import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockPackageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class) public class ToInfoMapperShould {
  @InjectMocks protected ToInfoMapper toInfoMapper;

  @Test public void mapApplicationInfoToAppInfo() {
    AppInfo app = toInfoMapper.map(MockPackageInfo.TEST);

    assertThat(app).isEqualTo(MockApp.TEST_INFO);
  }
}
