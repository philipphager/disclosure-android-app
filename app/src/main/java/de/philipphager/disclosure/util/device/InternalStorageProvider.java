package de.philipphager.disclosure.util.device;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import java.io.File;
import javax.inject.Inject;

public class InternalStorageProvider implements StorageProvider {
  private final Context context;

  @Inject public InternalStorageProvider(Context context) {
    this.context = context;
  }

  @Override public File getRootDir() {
    return ContextCompat.getDataDir(context);
  }
}
