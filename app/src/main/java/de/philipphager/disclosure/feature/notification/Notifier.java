package de.philipphager.disclosure.feature.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.app.NotificationCompat;
import javax.inject.Inject;

public class Notifier {
  private final Context context;
  private final NotificationManager notificationManager;

  @Inject public Notifier(Context context, NotificationManager notificationManager) {
    this.context = context;
    this.notificationManager = notificationManager;
  }

  public void show(@DrawableRes int icon, String title, String description) {
    Notification notification = new NotificationCompat.Builder(context)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(description)
        .build();

    notificationManager.notify(1, notification);
  }
}
