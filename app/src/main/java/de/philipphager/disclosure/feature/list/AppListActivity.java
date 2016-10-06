package de.philipphager.disclosure.feature.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseActivity;

public class AppListActivity extends BaseActivity {
  public static Intent launch(Context context) {
    return new Intent(context, AppListActivity.class);
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_list);
  }
}
