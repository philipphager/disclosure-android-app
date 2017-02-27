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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.kofigyan.stateprogressbar.StateProgressBar;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.app.detail.tutorials.EditPermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.PermissionExplanationDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.RuntimePermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.usecase.LibraryWithPermission;
import de.philipphager.disclosure.util.ui.BaseActivity;
import de.philipphager.disclosure.util.ui.components.ProtectionLevelView;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.List;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DetailActivity extends BaseActivity implements DetailView {
  private static final String EXTRA_APP = "EXTRA_APP";
  private static final String EDIT_PERMISSIONS_DIALOG = "EDIT_PERMISSIONS_DIALOG";
  private static final String PERMISSIONS_UNSUPPORTED_DIALOG = "PERMISSIONS_UNSUPPORTED_DIALOG";
  private static final String PERMISSION_EXPLANATION_DIALOG = "PERMISSION_EXPLANATION_DIALOG";

  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.app_title) protected TextView appTitle;
  @BindView(R.id.activity_detail) protected CoordinatorLayout rootView;
  @BindView(R.id.app_detail_libraries) protected RecyclerView libraryListRecyclerView;
  @BindView(R.id.btn_edit_settings) protected Button btnEditSettings;
  @BindView(R.id.btn_trust) protected Button btnToggleTrust;
  @BindView(R.id.btn_analyse_app) protected FloatingActionButton btnAnalyseApp;
  @BindView(R.id.analysis_progress) protected StateProgressBar analysisProgressBar;
  @BindInt(android.R.integer.config_shortAnimTime) protected int shortAnimTime;
  @Inject protected DetailPresenter presenter;
  private LibraryRecyclerAdapter adapter;

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
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
    ensureNotNull(app, "DetailActivity started without EXTRA_APP");
    presenter.onCreate(this, app);
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

  @Override public void notify(String message) {
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

  @Override public void showPermissionExplanation(String packageName, Permission permission) {
    PermissionExplanationDialog.newInstance(packageName, permission)
        .show(getSupportFragmentManager(), PERMISSION_EXPLANATION_DIALOG);
  }

  @Override public void enableEditPermissions(boolean isEnabled) {
    int id = isEnabled ? R.drawable.ic_edit : R.drawable.ic_edit_disabled;
    int text = isEnabled ? R.color.icon : R.color.icon_disabled;

    Drawable editIcon = ResourcesCompat.getDrawable(getResources(), id, null);
    btnEditSettings.setCompoundDrawablesWithIntrinsicBounds(null, editIcon, null, null);
    btnEditSettings.setTextColor(ContextCompat.getColor(this, text));
  }

  @Override public void setAppIsTrusted(boolean isTrusted) {
    int id = isTrusted ? R.drawable.ic_lock : R.drawable.ic_lock_open;
    int textColor = isTrusted ? R.color.colorPrimary : R.color.icon;
    String text = isTrusted ? getString(R.string.app_detail_action_trust)
        : getString(R.string.app_detail_action_distrust);

    Drawable editIcon = ResourcesCompat.getDrawable(getResources(), id, null);
    btnToggleTrust.setCompoundDrawablesWithIntrinsicBounds(null, editIcon, null, null);
    btnToggleTrust.setTextColor(ContextCompat.getColor(this, textColor));
    btnToggleTrust.setText(text);
  }

  @Override public void showAnalysisProgress() {
    TransitionManager.beginDelayedTransition(rootView);
    icon.setVisibility(View.GONE);
    appTitle.setVisibility(View.GONE);
    analysisProgressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideAnalysisProgress() {
    TransitionManager.beginDelayedTransition(rootView);
    icon.setVisibility(View.VISIBLE);
    appTitle.setVisibility(View.VISIBLE);
    analysisProgressBar.setVisibility(View.GONE);
  }

  @Override public void setAnalysisProgress(State state) {
    switch (state) {
      case DECOMPILATION:
        analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        break;
      case EXTRACTION:
        analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        break;
      case ANALYSIS:
        analysisProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        break;
      default:
        throw new IllegalArgumentException(String.format("No progress state for %s", state));
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
    Snackbar snackbar = Snackbar.make(rootView, R.string.activity_detail_analysis_in_progress,
        Snackbar.LENGTH_LONG);
    snackbar.setAction(R.string.action_cancel, v -> presenter.cancelAnalyseApp());
    snackbar.show();
  }

  @Override public void setScore(ProtectionLevelView.ProtectionLevel protectionLevel) {
    // Might be depricated
    // TODO: REMOVE
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

  @OnClick(R.id.btn_trust) public void onTrustAppClicked() {
    presenter.onTrustAppClicked();
  }
}
