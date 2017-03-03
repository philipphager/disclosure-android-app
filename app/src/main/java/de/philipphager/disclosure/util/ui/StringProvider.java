package de.philipphager.disclosure.util.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import javax.inject.Inject;

public class StringProvider {
  private final Context context;

  @Inject public StringProvider(Context context) {
    this.context = context;
  }

  public String getString(@StringRes int resId, Object... formatArgs) {
    return context.getString(resId, formatArgs);
  }
}
