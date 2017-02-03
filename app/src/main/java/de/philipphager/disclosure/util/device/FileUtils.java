package de.philipphager.disclosure.util.device;

import java.io.File;
import javax.inject.Inject;
import timber.log.Timber;

public class FileUtils {
  @Inject public FileUtils() {
  }

  public void delete(File file) {
    if (file.isDirectory()) {
      for (File c : file.listFiles())
        delete(c);
    }

    if (!file.delete()) {
      Timber.e("failed to delete file %s", file);
    }
  }
}
