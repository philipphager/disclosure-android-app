package de.philipphager.disclosure.database.library.model;

import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.database.mocks.MockLibrary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class) public class LibraryAppShould {
  @Test(expected = NullPointerException.class) public void throwErrorIfAppIdIsNull() {
    LibraryApp.create(null, MockLibrary.TEST.id());
  }

  @Test(expected = NullPointerException.class) public void throwErrorIfLibraryIdIsNull() {
    LibraryApp.create(MockApp.TEST.id(), null);
  }
}
