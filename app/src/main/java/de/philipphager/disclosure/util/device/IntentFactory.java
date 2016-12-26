package de.philipphager.disclosure.util.device;

import android.content.Intent;
import android.net.Uri;
import javax.inject.Inject;

public class IntentFactory {
  public Intent uninstallPackage(final String packageName) {
    Uri packageUri = Uri.parse(String.format("package:%s", packageName));

    Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
    intent.setData(packageUri);
    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
    return intent;
  }

  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public IntentFactory() {
    // Needed for dagger injection.
  }
}
