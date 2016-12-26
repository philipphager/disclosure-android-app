package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.text.Spanned;
import de.philipphager.disclosure.feature.navigation.Navigates;

public interface RuntimePermissionsTutorialView extends Navigates {
  void setSubtitle(Spanned spanned);

  void setDescription(Spanned spanned);
}
