package de.philipphager.disclosure.feature.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.ui.BaseActivity;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DetailActivity extends BaseActivity {
  private static final String EXTRA_APP = "EXTRA_APP";

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
    App app = getIntent().getParcelableExtra(EXTRA_APP);
    ensureNotNull(app, "DetailActivity started without EXTRA_APP");
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }
}
