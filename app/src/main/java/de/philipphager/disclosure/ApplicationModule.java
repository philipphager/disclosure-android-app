package de.philipphager.disclosure;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class ApplicationModule {
  private final Application app;

  public ApplicationModule(Application app) {
    this.app = app;
  }

  @Provides @Singleton public Context provideContext() {
    return app;
  }
}
