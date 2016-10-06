package de.philipphager.disclosure.util.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.DisclosureApp;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.navigation.Navigates;
import de.philipphager.disclosure.feature.navigation.Navigator;
import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity implements Navigates {
  @Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;
  @Inject protected Navigator navigator;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inject();
  }

  @Override public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);

    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }
  }

  @Override public Navigator navigate() {
    return navigator;
  }

  protected abstract void injectActivity(ApplicationComponent appComponent);

  private void inject() {
    DisclosureApp application = (DisclosureApp) getApplication();
    injectActivity(application.getApplicationComponent());

    navigator.setActivity(this);
  }
}
