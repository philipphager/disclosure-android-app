package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.content.Context;
import android.net.Uri;
import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.BuildConfig;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.preference.ui.HasSeenEditPermissionsTutorial;
import de.philipphager.disclosure.util.text.HtmlCompat;
import javax.inject.Inject;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class EditPermissionsTutorialPresenter {
  private final Context context;
  private final Preference<Boolean> hasSeenDialog;
  private EditPermissionsTutorialView view;
  private String packageName;

  @Inject public EditPermissionsTutorialPresenter(Context context,
      @HasSeenEditPermissionsTutorial Preference<Boolean> hasSeenDialog) {
    this.context = context;
    this.hasSeenDialog = hasSeenDialog;
  }

  public void onCreate(EditPermissionsTutorialView view, String packageName) {
    this.packageName = ensureNotNull(packageName);
    this.view = ensureNotNull(view);

    initView();
    hasSeenDialog.set(true);
  }

  private void initView() {
    String descriptionHtml = context.getString(R.string.dialog_edit_permissions_tutorial_subtitle);
    view.setDescription(HtmlCompat.fromHtml(descriptionHtml));

    String path = "android.resource://" + BuildConfig.APPLICATION_ID + "/raw/edit_settings";
    Uri videoUri = Uri.parse(path);
    view.showVideo(videoUri);
  }

  public void onPositiveButtonClicked() {
    view.navigate().toAppSystemSettings(packageName);
  }
}
