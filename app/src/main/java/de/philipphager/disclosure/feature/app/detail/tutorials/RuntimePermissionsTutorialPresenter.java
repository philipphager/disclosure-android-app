package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.content.Context;
import android.os.Build;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.text.HtmlCompat;
import javax.inject.Inject;

public class RuntimePermissionsTutorialPresenter {
  private final Context context;
  private RuntimePermissionsTutorialView view;

  @Inject public RuntimePermissionsTutorialPresenter(Context context) {
    this.context = context;
  }

  public void onCreate(RuntimePermissionsTutorialView view) {
    this.view = view;

    initView();
  }

  private void initView() {
    String androidVersion = Build.VERSION.RELEASE;
    String subtitleHtml = context.getString(
        R.string.dialog_runtime_permissions_unsupported_subtitle, androidVersion);
    String descriptionHtml = context.getString(
        R.string.dialog_runtime_permissions_unsupported_description);

    view.setSubtitle(HtmlCompat.fromHtml(subtitleHtml));
    view.setDescription(HtmlCompat.fromHtml(descriptionHtml));
  }

  public void onCheckSystemUpdatesClicked() {
    view.navigate().toSystemUpdates();
  }
}
