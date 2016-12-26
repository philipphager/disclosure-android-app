package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.net.Uri;
import android.text.Spanned;
import de.philipphager.disclosure.feature.navigation.Navigates;

public interface EditPermissionsTutorialView extends Navigates {
  void setDescription(Spanned description);

  void showVideo(Uri videoUri);
}
