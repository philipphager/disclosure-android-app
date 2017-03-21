package de.philipphager.disclosure.feature.app.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.kofigyan.stateprogressbar.StateProgressBar;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.analyser.AnalyticsProgress;
import de.philipphager.disclosure.feature.app.detail.tutorials.EditPermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.PermissionExplanationDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.RuntimePermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.usecase.LibraryWithPermission;
import de.philipphager.disclosure.util.ui.BaseActivity;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.List;
import javax.inject.Inject;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class AppDetailActivity extends BaseActivity implements DetailView {
  private static final String EXTRA_APP = "EXTRA_APP";
  private static final String EDIT_PERMISSIONS_DIALOG = "EDIT_PERMISSIONS_DIALOG";
  private static final String PERMISSIONS_UNSUPPORTED_DIALOG = "PERMISSIONS_UNSUPPORTED_DIALOG";
  private static final String PERMISSION_EXPLANATION_DIALOG = "PERMISSION_EXPLANATION_DIALOG";

  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.app_title) protected TextView appTitle;
  @BindView(R.id.activity_detail) protected CoordinatorLayout rootView;
  @BindView(R.id.app_detail_libraries) protected RecyclerView libraryListRecyclerView;
  @BindView(R.id.btn_edit_settings) protected Button btnEditSettings;
  @BindView(R.id.btn_analyse_app) protected FloatingActionButton btnAnalyseApp;
  @BindView(R.id.btn_show_all_permissions) protected SwitchCompat btnShowAllPermissions;
  @BindView(R.id.analysis_progress) protected StateProgressBar analysisProgressBar;
  @BindInt(android.R.integer.config_shortAnimTime) protected int shortAnimTime;
  @Inject protected AppDetailPresenter presenter;
  private LibraryRecyclerAdapter adapter;

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, AppDetailActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_app_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    adapter = new LibraryRecyclerAdapter();
    libraryListRecyclerView.setAdapter(adapter);
    libraryListRecyclerView.setHasFixedSize(true);
    libraryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    libraryListRecyclerView.setItemAnimator(new DefaultItemAnimator());

    adapter.setOnLibraryClickListener(library -> {
      presenter.onLibraryClicked(library);
    });

    adapter.setOnPermissionClickListener(permission -> {
      presenter.onPermissionClicked(permission);
    });

    String[] analysisSteps = new String[] {"Extraction", "Filtration", "Analysis"};
    analysisProgressBar.setStateDescriptionData(analysisSteps);

    App app = getIntent().getParcelableExtra(EXTRA_APP);
    ensureNotNull(app, "AppDetailActivity started without EXTRA_APP");
    presenter.onCreate(this, app);
  }

  @Override protected void onResume() {
    super.onResume();
    presenter.onResume();
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();

    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    presenter.onActivityResult(requestCode, resultCode);
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void setToolbarTitle(String title) {
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    appTitle.setText(title);
  }

  @Override public void setAppIcon(String packageName) {
    AppIconLoader.with(this)
        .load(packageName)
        .onThread(Schedulers.io())
        .into(icon);
  }

  @Override public void setLibraries(List<LibraryWithPermission> libraries) {
    adapter.setLibraries(libraries);
  }

  @Override public void setShowAllLibraries(boolean isChecked) {
    btnShowAllPermissions.setChecked(isChecked);
  }

  @Override public void notify(String message) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void notifyAnalysisResult(String appLabel, int permissionCount, int libraryCount) {
    String message = String
        .format(getString(R.string.notify_analyse_apps_result), appLabel, permissionCount,
            libraryCount);
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
  }

  @Override public void showEditPermissionsTutorial(String packageName) {
    EditPermissionsTutorialDialog.newInstance(packageName)
        .show(getSupportFragmentManager(), EDIT_PERMISSIONS_DIALOG);
  }

  @Override public void showRuntimePermissionsTutorial(String packageName) {
    RuntimePermissionsTutorialDialog.newInstance()
        .show(getSupportFragmentManager(), PERMISSIONS_UNSUPPORTED_DIALOG);
  }

  @Override public void showPermissionExplanation(App app, Permission permission) {
    PermissionExplanationDialog.newInstance(app, permission)
        .show(getSupportFragmentManager(), PERMISSION_EXPLANATION_DIALOG);
  }

  @Override public void enableEditPermissions(boolean isEnabled) {
    int id = isEnabled ? R.drawable.ic_edit : R.drawable.ic_edit_disabled;
    int text = isEnabled ? R.color.color_icon : R.color.color_icon_disabled;

    Drawable editIcon = ResourcesCompat.getDrawable(getResources(), id, null);
    btnEditSettings.setCompoundDrawablesWithIntrinsicBounds(null, editIcon, null, null);
    btnEditSettings.setTextColor(ContextCompat.getColor(this, text));
  }

  @Override public void showAnalysisProgress() {
    TransitionManager.beginDelayedTransition(rootView);
    icon.setVisibility(View.GONE);
    appTitle.setVisibility(View.GONE);
    analysisProgressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideAnalysisProgress() {
    TransitionManager.beginDelayedTransition(rootView);
    analysisProgressBar.setVisibility(View.GONE);
    icon.setVisibility(View.VISIBLE);
    appTitle.setVisibility(View.VISIBLE);
  }

  @Override public void setAnalysisProgress(AnalyticsProgress.State state) {
    if (state == AnalyticsProgress.State.COMPLETE || state == AnalyticsProgress.State.CANCEL) {
      hideAnalysisProgress();
    } else {
      showAnalysisProgress();
    }

    switch (state) {
      case START:
        resetProgress();
        break;
      case EXTRACT_APK:
        analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        break;
      case FILTER_METHODS:
        analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        break;
      case ANALYSE_METHODS:
        analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        break;
      case COMPLETE:
        setAnalysisCompleted();
        break;
      default:
        Timber.d("Unhandled state %s", state);
        break;
    }
  }

  @Override public void setAnalysisCompleted() {
    analysisProgressBar.setAllStatesCompleted(true);
  }

  @Override public void resetProgress() {
    analysisProgressBar.setAllStatesCompleted(false);
    analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
  }

  @Override public void showCancel() {
    Snackbar snackbar = Snackbar.make(rootView, R.string.notify_analyse_apps_in_progress,
        Snackbar.LENGTH_LONG);
    snackbar.setAction(R.string.action_cancel, v -> presenter.cancelAnalyseApp());
    snackbar.show();
  }

  @Override public void showPendingApp(App app) {
    String message = getString(R.string.notify_analyse_app_pending, app.label());
    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
    snackbar.setAction(R.string.action_undo, v -> presenter.cancelAnalyseApp());
    snackbar.show();
  }

  @OnClick(R.id.btn_analyse_app) public void onAnalyseAppClicked() {
    presenter.onAnalyseAppClicked();
  }

  @OnClick(R.id.btn_edit_settings) public void onEditSettingsClicked() {
    presenter.onEditPermissionsClicked();
  }

  @OnLongClick(R.id.btn_edit_settings) public boolean onEditSettingsLongClicked() {
    return presenter.onEditPermissionsLongClicked();
  }

  @OnClick(R.id.btn_uninstall) public void onUninstallClicked() {
    presenter.onUninstallClicked();
  }

  @OnClick(R.id.btn_open_app) public void onTrustAppClicked() {
    presenter.openOpenAppClicked();
  }

  @OnCheckedChanged(R.id.btn_show_all_permissions)
  public void onToggleShowAllPermissions(boolean isChecked) {
    presenter.onToggleShowAllPermissions(isChecked);
  }
}
