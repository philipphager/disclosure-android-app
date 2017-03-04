package de.philipphager.disclosure.feature.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.app.detail.DetailActivity;
import de.philipphager.disclosure.feature.app.manager.search.SearchActivity;
import de.philipphager.disclosure.feature.home.HomeActivity;
import de.philipphager.disclosure.feature.library.detail.LibraryDetailActivity;
import javax.inject.Inject;

public class Navigator {
  private Activity activity;

  @SuppressWarnings("PMD.UnnecessaryConstructor") @Inject public Navigator() {
    // Needed for dagger injection.
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public void toHome() {
    activity.startActivity(HomeActivity.launch(activity));
  }

  public void toSearch() {
    activity.startActivity(SearchActivity.launch(activity));
  }

  public void toAppDetail(App app) {
    activity.startActivity(DetailActivity.launch(activity, app));
  }

  public void toLibraryDetail(Library library) {
    activity.startActivity(LibraryDetailActivity.launch(activity, library));
  }

  public void toAppSystemSettings(String packageName) {
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    activity.startActivity(intent);
  }

  public void toSystemUpdates() {
    Intent intent = new Intent("android.settings.SYSTEM_UPDATE_SETTINGS");
    activity.startActivity(intent);
  }

  public void toGooglePlay(String packageName) {
    try {
      Uri appUri = Uri.parse("market://details?id=" + packageName);
      activity.startActivity(new Intent(Intent.ACTION_VIEW, appUri));
    } catch (android.content.ActivityNotFoundException anfe) {
      Uri appUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
      activity.startActivity(new Intent(Intent.ACTION_VIEW, appUri));
    }
  }

  public void toWebsite(String websiteUrl) {
    Uri uri = Uri.parse(websiteUrl);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(uri);
    activity.startActivity(intent);
  }
}
