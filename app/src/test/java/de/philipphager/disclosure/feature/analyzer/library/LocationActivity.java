package de.philipphager.disclosure.feature.analyzer.library;

import de.philipphager.disclosure.database.method.model.Method;
import java.util.Arrays;
import java.util.List;

public final class LocationActivity {
  public static final List<Method> INVOKED_METHODS = Arrays.asList(
      Method.builder()
          .name("<init>")
          .declaringType("Landroid/support/v7/app/AppCompatActivity;")
          .returnType("V")
          .argTypes("")
          .build(),
      Method.builder()
          .name("onStart")
          .declaringType("Landroid/support/v7/app/AppCompatActivity;")
          .returnType("V")
          .argTypes("Landroid/os/Bundle;")
          .build(),
      Method.builder()
          .name("setContentView")
          .declaringType("Lde/philipphager/location/MainActivity;")
          .returnType("V")
          .argTypes("I")
          .build(),
      Method.builder()
          .name("checkSelfPermission")
          .declaringType("Landroid/support/v4/app/ActivityCompat;")
          .returnType("I")
          .argTypes("Landroid/content/Context;Ljava/lang/String;")
          .build(),
      Method.builder()
          .name("checkSelfPermission")
          .declaringType("Landroid/support/v4/app/ActivityCompat;")
          .returnType("I")
          .argTypes("Landroid/content/Context;Ljava/lang/String;")
          .build(),
      Method.builder()
          .name("requestPermissions")
          .declaringType("Landroid/support/v4/app/ActivityCompat;")
          .returnType("V")
          .argTypes("Landroid/app/Activity;[Ljava/lang/String;I")
          .build(),
      Method.builder()
          .name("getSystemService")
          .declaringType("Lde/philipphager/location/MainActivity;")
          .returnType("Ljava/lang/Object;")
          .argTypes("Ljava/lang/String;")
          .build(),
      Method.builder()
          .name("<init>")
          .declaringType("Lde/philipphager/location/MainActivity$1;")
          .returnType("V")
          .argTypes("Lde/philipphager/location/MainActivity;")
          .build(),
      Method.builder()
          .name("requestLocationUpdates")
          .declaringType("Landroid/location/LocationManager;")
          .returnType("V")
          .argTypes("Ljava/lang/String;JFLandroid/location/LocationListener;")
          .build()
  );

  private LocationActivity() {
    // No instances.
  }
}
