package de.philipphager.disclosure.feature.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.ui.BaseActivity;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DetailActivity extends BaseActivity implements DetailView {
  private static final String EXTRA_APP = "EXTRA_APP";

  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.activity_detail) protected View view;
  @Inject protected DetailPresenter presenter;

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    App app = getIntent().getParcelableExtra(EXTRA_APP);
    ensureNotNull(app, "DetailActivity started without EXTRA_APP");

    presenter.onCreate(this, app);
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void setToolbarTitle(String title) {
    getSupportActionBar().setTitle(title);
  }

  @Override public void setAppIcon(String packageName) {
    AppIconLoader.with(this)
        .load(packageName)
        .onThread(Schedulers.io())
        .into(icon);
  }

  @Override public void notify(String message) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
  }

  @OnClick(R.id.btn_analyse) public void onAnalyseButtonClick() {
    presenter.onAnalyseButtonClick();
  }
}
