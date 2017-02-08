package de.philipphager.disclosure.feature.app.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.app.detail.tutorials.EditPermissionsTutorialDialog;
import de.philipphager.disclosure.feature.app.detail.tutorials.RuntimePermissionsTutorialDialog;
import de.philipphager.disclosure.util.ui.BaseActivity;
import de.philipphager.disclosure.util.ui.components.ScoreView;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.List;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DetailActivity extends BaseActivity implements DetailView {
  private static final String EXTRA_APP = "EXTRA_APP";
  private static final String EDIT_PERMISSIONS_DIALOG = "EDIT_PERMISSIONS_DIALOG";
  private static final String PERMISSIONS_UNSUPPORTED_DIALOG = "PERMISSIONS_UNSUPPORTED_DIALOG";

  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.app_title) protected TextView appTitle;
  @BindView(R.id.activity_detail) protected View view;
  @BindView(R.id.app_detail_libraries) protected RecyclerView libraryListRecyclerView;
  @BindView(R.id.btn_edit_settings) protected Button btnEditSettings;
  @BindView(R.id.btn_trust) protected Button btnToggleTrust;
  @Inject protected DetailPresenter presenter;
  private LibraryRecyclerAdapter adapter;

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    adapter = new LibraryRecyclerAdapter();
    libraryListRecyclerView.setAdapter(adapter);
    libraryListRecyclerView.setHasFixedSize(true);
    libraryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    libraryListRecyclerView.setItemAnimator(new DefaultItemAnimator());

    adapter.setOnLibraryClickListener(library -> {
      presenter.onLibraryClicked(library);
    });

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

  @Override public void setLibraries(List<Library> libraries) {
    adapter.setLibraries(libraries);
  }

  @Override public void notify(String message) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
  }

  @Override public void showEditPermissionsTutorial(String packageName) {
    EditPermissionsTutorialDialog.newInstance(packageName)
        .show(getSupportFragmentManager(), EDIT_PERMISSIONS_DIALOG);
  }

  @Override public void showRuntimePermissionsTutorial(String packageName) {
    RuntimePermissionsTutorialDialog.newInstance()
        .show(getSupportFragmentManager(), PERMISSIONS_UNSUPPORTED_DIALOG);
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

  @Override public void setScore(ScoreView.Score score) {

  }

  //@OnClick(R.id.btn_analyse) public void onAnalyseButtonClicked() {
  //  presenter.onAnalyseClicked();
  //}

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
