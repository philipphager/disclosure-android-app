package de.philipphager.disclosure;

import dagger.Component;
import de.philipphager.disclosure.database.DatabaseModule;
import de.philipphager.disclosure.feature.list.AppListActivity;
import de.philipphager.disclosure.feature.receivers.AppUninstallReceiver;
import de.philipphager.disclosure.feature.device.AndroidModule;
import javax.inject.Singleton;

@Singleton @Component(modules = { ApplicationModule.class, DatabaseModule.class, AndroidModule.class })
public interface ApplicationComponent {
  void inject(AppListActivity activity);
}
