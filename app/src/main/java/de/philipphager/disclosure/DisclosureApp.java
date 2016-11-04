package de.philipphager.disclosure;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DisclosureApp extends Application {
  private ApplicationComponent applicationComponent;

  @Override public void onCreate() {
    super.onCreate();

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);

    fixUserManagerMemoryLeak();

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


  /**
   * TODO: Remove on update!
   * Avoiding a memory leak of Android's UserManager. It is caching a static
   * instance and leaks, on Activity recreation, unless UserManager.get()
   * is called before. This is a temporary prevention a specific memory
   * leak and should be removed instantly after upgrading Android.
   */
  private void fixUserManagerMemoryLeak() {
    getPackageManager().getUserBadgedLabel("", android.os.Process.myUserHandle());
  }
}
