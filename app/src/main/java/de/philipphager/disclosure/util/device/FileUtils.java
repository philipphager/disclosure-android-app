package de.philipphager.disclosure.util.device;

import java.io.File;
import javax.inject.Inject;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class FileUtils {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public FileUtils() {
    // Needed for dagger injection.
  }

  public void delete(File file) {
    ensureNotNull(file, "Attempt to delete null file");

    if (file.isDirectory()) {
      File[] files = file.listFiles();

      if (files != null) {
        for (File c : files) {
          delete(c);
        }
      }
    }

    if (!file.delete()) {
      Timber.e("failed to delete file %s", file);
    }
  }
}
