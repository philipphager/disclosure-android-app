package de.philipphager.disclosure.feature.sync.db.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.philipphager.disclosure.DisclosureApp;
import de.philipphager.disclosure.feature.sync.db.DBSyncer;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class PackageManagerBroadcastReceiver extends BroadcastReceiver {
  @Inject protected DBSyncer dbSyncer;

  @Override public void onReceive(Context context, Intent intent) {
    injectThis(context);

    dbSyncer.sync()
        .subscribeOn(Schedulers.io())
        .subscribe();
  }

  private void injectThis(Context context) {
    DisclosureApp application = (DisclosureApp) context.getApplicationContext();
    application.getApplicationComponent().inject(this);
  }
}
