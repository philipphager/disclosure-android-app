package de.philipphager.disclosure;

import android.app.Application;

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
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
}
