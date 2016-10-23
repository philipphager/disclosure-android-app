package de.philipphager.disclosure.util.assertion;

public final class Assertions {
  @SuppressWarnings("PMD.UnnecessaryConstructor") private Assertions() {
    //No instances of helper classes.
  }

  public static <T> T ensureNotNull(T t) {
    if (t == null) {
      throw new IllegalStateException("must not be null");
    }
    return t;
  }

  public static <T> T ensureNotNull(T t, String message) {
    if (t == null) {
      throw new IllegalStateException(message);
    }
    return t;
  }

  public static void check(boolean argument, String message) {
    if (!argument) {
      throw new IllegalStateException(message);
    }
  }
}
