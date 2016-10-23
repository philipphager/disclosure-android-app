package de.philipphager.disclosure;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DisclosureApp extends Application {
  private ApplicationComponent applicationComponent;

  @Override public void onCreate() {
    super.onCreate();
    buildObjectGraphAndInject();
  }

  private void buildObjectGraphAndInject() {
    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();

    AndroidThreeTen.init(this);

    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this);
      Timber.plant(new Timber.DebugTree());
    }
  }

  public ApplicationComponent getApplicationComponent() {
    return ensureNotNull(applicationComponent, "object graph not build");
  }
}
