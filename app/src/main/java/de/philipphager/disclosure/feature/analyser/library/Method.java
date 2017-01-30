package de.philipphager.disclosure.feature.analyser.library;

import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Method {
  public static Method create(String invocationType, String declaringType, String returnType,
      String name, List<String> argsType) {
    return new AutoValue_Method(invocationType, declaringType, returnType, name, argsType);
  }

  public abstract String invocationType();

  public abstract String declaringType();

  public abstract String returnType();

  public abstract String name();

  public abstract List<String> argsType();
}
