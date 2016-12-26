package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseDialogFragment;
import javax.inject.Inject;

public class RuntimePermissionsTutorialDialog extends BaseDialogFragment
    implements RuntimePermissionsTutorialView {
  @BindView(R.id.txtSubtitle) protected TextView txtSubtitle;
  @BindView(R.id.txtDescription) protected TextView txtDescription;
  @Inject protected RuntimePermissionsTutorialPresenter presenter;

  public static RuntimePermissionsTutorialDialog newInstance() {
    return new RuntimePermissionsTutorialDialog();
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = LayoutInflater.from(getContext())
        .inflate(R.layout.dialog_runtime_permissions_unsupported, null);
    ButterKnife.bind(this, view);

    presenter.onCreate(this);

    return new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_android)
        .setTitle(R.string.dialog_runtime_permissions_unsupported_title)
        .setNegativeButton(R.string.action_back, null)
        .setPositiveButton(R.string.action_check_system_updates,
            (dialogInterface, i) -> presenter.onCheckSystemUpdatesClicked())
        .setView(view)
        .create();
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void setSubtitle(Spanned spanned) {
    txtSubtitle.setText(spanned);
  }

  @Override public void setDescription(Spanned spanned) {
    txtDescription.setText(spanned);
  }
}
