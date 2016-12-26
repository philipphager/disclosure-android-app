package de.philipphager.disclosure.util.text;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public final class HtmlCompat {
  public static Spanned fromHtml(String source) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    } else {
      return Html.fromHtml(source);
    }
  }

  private HtmlCompat() {
    // no instances
  }
}
