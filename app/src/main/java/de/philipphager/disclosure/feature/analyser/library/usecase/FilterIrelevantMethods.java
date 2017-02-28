package de.philipphager.disclosure.feature.analyser.library.usecase;

import de.philipphager.disclosure.database.method.model.Method;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Func1;

public class FilterIrelevantMethods implements Func1<Method, Boolean> {
  private static final List<String> ANDROID_ORIGIN =
      Arrays.asList("Landroid", "Lcom/android", "Lcom/google", "Ljava", "Lorg/apache");

  @Inject public FilterIrelevantMethods() {
  }

  @Override public Boolean call(Method method) {
    boolean isAndroidApiMethod = false;

    for (int i = 0; i < ANDROID_ORIGIN.size() && !isAndroidApiMethod; i++) {
      isAndroidApiMethod = method.declaringType().startsWith(ANDROID_ORIGIN.get(i));
    }

    return isAndroidApiMethod;
  }
}
