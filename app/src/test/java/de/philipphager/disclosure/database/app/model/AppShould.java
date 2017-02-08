package de.philipphager.disclosure.database.app.model;

import de.philipphager.disclosure.database.mocks.MockApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class) public class AppShould {
  @Test public void useBuilderToSetAllFields() {
    App app = App.builder()
        .id(MockApp.TEST.id())
        .label(MockApp.TEST.label())
        .packageName(MockApp.TEST.packageName())
        .flags(MockApp.TEST.flags())
        .process(MockApp.TEST.process())
        .sourceDir(MockApp.TEST.sourceDir())
        .isTrusted(MockApp.TEST.isTrusted())
        .build();

    assertThat(app).isEqualTo(MockApp.TEST);
  }

  @Test public void allowIdToBeNull() {
    App app = App.builder()
        .label(MockApp.TEST.label())
        .packageName(MockApp.TEST.packageName())
        .flags(MockApp.TEST.flags())
        .process(MockApp.TEST.process())
        .sourceDir(MockApp.TEST.sourceDir())
        .isTrusted(MockApp.TEST.isTrusted())
        .build();

    assertThat(app.id()).isNull();
  }

  @Test(expected = IllegalStateException.class) public void throwErrorIfLabelIsNull() {
    App.builder()
        .packageName(MockApp.TEST.packageName())
        .flags(MockApp.TEST.flags())
        .process(MockApp.TEST.process())
        .sourceDir(MockApp.TEST.sourceDir())
        .isTrusted(MockApp.TEST.isTrusted())
        .build();
  }

  @Test(expected = IllegalStateException.class) public void throwErrorIfPackageNameIsNull() {
    App.builder()
        .label(MockApp.TEST.label())
        .flags(MockApp.TEST.flags())
        .process(MockApp.TEST.process())
        .sourceDir(MockApp.TEST.sourceDir())
        .isTrusted(MockApp.TEST.isTrusted())
        .build();
  }
}
