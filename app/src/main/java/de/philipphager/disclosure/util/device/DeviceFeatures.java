package de.philipphager.disclosure.util.device;

import android.os.Build;
import de.philipphager.disclosure.feature.device.AndroidOsVersion;
import javax.inject.Inject;

public final class DeviceFeatures {
  private final int osVersion;

  public boolean supportsRuntimePermissions() {
    return osVersion >= Build.VERSION_CODES.M;
  }

  @Inject public DeviceFeatures(@AndroidOsVersion int osVersion) {
    this.osVersion = osVersion;
  }
}
