package de.philipphager.disclosure.feature.app.detail.tutorials;

import android.app.Dialog;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.util.ui.BaseDialogFragment;
import de.philipphager.disclosure.util.ui.components.PermissionGrantedView;
import de.philipphager.disclosure.util.ui.components.ProtectionLevelView;
import javax.inject.Inject;

public class PermissionExplanationDialog extends BaseDialogFragment
    implements PermissionExplanationDialogView {
  private static final String EXTRA_APP = "EXTRA_APP";
  private static final String EXTRA_PERMISSION = "EXTRA_PERMISSION";
  @Inject protected PermissionExplanationDialogPresenter presenter;
  @BindView(R.id.txt_description) TextView txtDescription;
  @BindView(R.id.status) PermissionGrantedView status;
  @BindView(R.id.txt_hint) TextView txtHint;

  public static PermissionExplanationDialog newInstance(App app, Permission permission) {
    PermissionExplanationDialog fragment = new PermissionExplanationDialog();
    Bundle args = new Bundle();
    args.putParcelable(EXTRA_APP, app);
    args.putParcelable(EXTRA_PERMISSION, permission);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = LayoutInflater.from(getContext())
        .inflate(R.layout.dialog_permission_explanation, null);

    ButterKnife.bind(this, view);

    ProtectionLevelView protectionLevelView =
        (ProtectionLevelView) view.findViewById(R.id.protection_level);

    TextView description = (TextView) view.findViewById(R.id.txt_description);

    Permission permission = getPermission();
    ProtectionLevelView.ProtectionLevel protectionLevel =
        getProtectionLevel(permission.protectionLevel());

    presenter.onCreate(this, getApp(), getPermission());

    protectionLevelView.setProtectionLevel(protectionLevel);
    description.setText(permission.description());

    return getDialog(view, permission, presenter.permissionCanBeRevoked());
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  private Permission getPermission() {
    return ((Permission) getArguments().getParcelable(EXTRA_PERMISSION));
  }

  private App getApp() {
    return ((App) getArguments().getParcelable(EXTRA_APP));
  }

  private ProtectionLevelView.ProtectionLevel getProtectionLevel(int level) {
    switch (level) {
      case PermissionInfo.PROTECTION_NORMAL:
        return ProtectionLevelView.ProtectionLevel.NORMAL;
      case PermissionInfo.PROTECTION_DANGEROUS:
        return ProtectionLevelView.ProtectionLevel.DANGEROUS;
      case PermissionInfo.PROTECTION_SIGNATURE:
        return ProtectionLevelView.ProtectionLevel.SIGNATURE;
      default:
        throw new IllegalArgumentException("No protection level for " + level);
    }
  }

  private AlertDialog getDialog(View view, Permission permission, boolean canBeRevoked) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
        .setTitle(permission.title())
        .setNegativeButton(R.string.action_back, null)
        .setView(view);

    if (canBeRevoked) {
      alertDialog.setPositiveButton(R.string.action_revoke_permission, (dialog, which) ->
          presenter.onRevokePermissionClicked());
    }

    return alertDialog.create();
  }

  @Override public void showHint(String text) {
    txtHint.setText(text);
    txtHint.setVisibility(View.VISIBLE);
  }

  @Override public void showPermissionStatus(boolean isGranted, boolean canBeRevoked) {
    status.setPermissionGranted(isGranted, canBeRevoked);
  }
}
