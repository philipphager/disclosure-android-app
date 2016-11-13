package de.philipphager.disclosure.database.library.model;

import de.philipphager.disclosure.database.app.mocks.MockFeature;
import de.philipphager.disclosure.database.app.mocks.MockLibrary;
import org.junit.Test;

public class LibraryFeatureShould {
  @Test(expected = NullPointerException.class) public void throwErrorIfIdIsNull() {
    LibraryFeature.create(null,
        MockLibrary.TEST.id(),
        MockFeature.TEST.id(),
        MockFeature.TEST.createdAt(),
        MockFeature.TEST.updatedAt());
  }

  @Test(expected = NullPointerException.class) public void throwErrorIfLibraryIdIsNull() {
    LibraryFeature.create("test-id",
        null,
        MockFeature.TEST.id(),
        MockFeature.TEST.createdAt(),
        MockFeature.TEST.updatedAt());
  }

  @Test(expected = NullPointerException.class) public void throwErrorIfFeatureIdIsNull() {
    LibraryFeature.create("test-id",
        MockLibrary.TEST.id(),
        null,
        MockFeature.TEST.createdAt(),
        MockFeature.TEST.updatedAt());
  }
}
