package de.philipphager.disclosure.util.device;

import android.os.Build;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DeviceFeaturesShould {
  @Test public void notSupportRuntimePermissionsBeforeAndroidMarshmallow() {
    DeviceFeatures deviceFeatures = new DeviceFeatures(Build.VERSION_CODES.LOLLIPOP_MR1);
    assertThat(deviceFeatures.supportsRuntimePermissions()).isFalse();
  }

  @Test public void supportRuntimePermissionsOnMarshmallowAndHigher() {
    DeviceFeatures deviceFeatures = new DeviceFeatures(Build.VERSION_CODES.M);
    assertThat(deviceFeatures.supportsRuntimePermissions()).isTrue();
  }
}
