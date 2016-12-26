package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseDialogFragment;
import javax.inject.Inject;

public class EditPermissionsTutorialDialog extends BaseDialogFragment
    implements EditPermissionsTutorialView {
  private static final String EXTRA_PACKAGE = "EXTRA_PACKAGE";

  @Nullable @BindView(R.id.txtDescription) protected TextView txtDescription;
  @BindView(R.id.video) protected VideoView videoView;
  @Inject protected EditPermissionsTutorialPresenter presenter;

  public static EditPermissionsTutorialDialog newInstance(String packageName) {
    Bundle args = new Bundle();
    args.putString(EXTRA_PACKAGE, packageName);

    EditPermissionsTutorialDialog fragment = new EditPermissionsTutorialDialog();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = LayoutInflater.from(getContext())
        .inflate(R.layout.dialog_edit_permissions, null);
    ButterKnife.bind(this, view);

    String packageName = getArguments().getString(EXTRA_PACKAGE);
    presenter.onCreate(this, packageName);

    return new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_settings)
        .setTitle(R.string.dialog_edit_permissions_tutorial_title)
        .setPositiveButton(R.string.action_got_it,
            (dialogInterface, i) -> presenter.onPositiveButtonClicked())
        .setNegativeButton(R.string.action_back, null)
        .setView(view)
        .create();
  }

  @Override public void setDescription(Spanned description) {
    // Optional description in landscape.
    if (txtDescription != null) {
      txtDescription.setText(description);
    }
  }

  @Override public void showVideo(Uri videoUri) {
    videoView.setVideoURI(videoUri);
    videoView.setOnPreparedListener(mp -> mp.setLooping(true));
    videoView.setZOrderOnTop(true);
    videoView.start();
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }
}
