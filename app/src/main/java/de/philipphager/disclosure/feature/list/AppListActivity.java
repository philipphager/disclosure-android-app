package de.philipphager.disclosure.feature.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseActivity;
import javax.inject.Inject;

public class AppListActivity extends BaseActivity implements AppListView {
  @BindView(R.id.activity_app_list) protected View content;
  @Inject protected AppListPresenter presenter;

  public static Intent launch(Context context) {
    return new Intent(context, AppListActivity.class);
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_list);
    presenter.onCreate(this);
  }

  @Override public void notify(String message) {
    Snackbar.make(content, message, Snackbar.LENGTH_LONG).show();
  }
}
