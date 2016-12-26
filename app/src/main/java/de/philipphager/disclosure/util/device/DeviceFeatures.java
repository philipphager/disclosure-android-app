package de.philipphager.disclosure.util.device;

import android.os.Build;

public final class DeviceFeatures {
  public static boolean supportsRuntimePermissions() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
  }

  private DeviceFeatures() {
    // Not instances.
  }
}
