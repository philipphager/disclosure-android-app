package de.philipphager.disclosure.feature.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import butterknife.BindView;
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

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @BindView(R.id.icon) ImageView icon;
  @Inject DetailPresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
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
}
