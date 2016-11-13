package de.philipphager.disclosure.database.library.model;

import de.philipphager.disclosure.database.app.mocks.MockLibrary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class) public class LibraryShould {
  @Test public void useBuilderToSetAllFields() throws Exception {
    Library library = Library.builder()
        .id(MockLibrary.TEST.id())
        .packageName(MockLibrary.TEST.packageName())
        .title(MockLibrary.TEST.title())
        .subtitle(MockLibrary.TEST.subtitle())
        .description(MockLibrary.TEST.description())
        .websiteUrl(MockLibrary.TEST.websiteUrl())
        .type(MockLibrary.TEST.type())
        .createdAt(MockLibrary.TEST.createdAt())
        .updatedAt(MockLibrary.TEST.updatedAt())
        .build();

    assertThat(library).isEqualTo(MockLibrary.TEST);
  }

  @Test(expected = IllegalStateException.class) public void throwErrorIfIdIsNull() {
    Library.builder()
        .packageName(MockLibrary.TEST.packageName())
        .title(MockLibrary.TEST.title())
        .subtitle(MockLibrary.TEST.subtitle())
        .description(MockLibrary.TEST.description())
        .websiteUrl(MockLibrary.TEST.websiteUrl())
        .type(MockLibrary.TEST.type())
        .createdAt(MockLibrary.TEST.createdAt())
        .updatedAt(MockLibrary.TEST.updatedAt())
        .build();
  }

  @Test(expected = IllegalStateException.class) public void throwErrorIfPackageNameIsNull() {
    Library.builder()
        .id(MockLibrary.TEST.id())
        .title(MockLibrary.TEST.title())
        .subtitle(MockLibrary.TEST.subtitle())
        .description(MockLibrary.TEST.description())
        .websiteUrl(MockLibrary.TEST.websiteUrl())
        .type(MockLibrary.TEST.type())
        .createdAt(MockLibrary.TEST.createdAt())
        .updatedAt(MockLibrary.TEST.updatedAt())
        .build();
  }
}
