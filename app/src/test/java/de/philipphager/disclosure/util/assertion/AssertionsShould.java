package de.philipphager.disclosure.util.assertion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AssertionsShould {
  @Test(expected = IllegalStateException.class)
  public void throwExceptionWhenEnsureNotNullsArgumentIsNull() throws IllegalStateException {
    Assertions.ensureNotNull(null);
  }

  @Test public void returnOriginalObjectWhenNotNull() {
    Object mockObject = new Object();

    assertThat(Assertions.ensureNotNull(mockObject)).isEqualTo(mockObject);
  }

  @Test(expected = IllegalStateException.class)
  public void throwExceptionWhenArgumentIsFalse() throws IllegalStateException {
    Assertions.check(false, "");
  }
}
