package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.list.AppListActivity;
import javax.inject.Singleton;

@Singleton @Component(modules = { ApplicationModule.class, DatabaseModule.class })
public interface ApplicationComponent {
  void inject(AppListActivity activity);
}
