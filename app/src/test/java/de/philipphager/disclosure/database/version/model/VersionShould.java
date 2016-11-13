package de.philipphager.disclosure.database.version.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

@RunWith(MockitoJUnitRunner.class) public class VersionShould {
  @Test(expected = NullPointerException.class) public void throwErrorIfVersionIdIsNull() {
    Version.create(null, 0, "", LocalDateTime.now());
  }
}
