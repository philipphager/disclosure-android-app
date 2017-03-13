package de.philipphager.disclosure.feature.library.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseActivity;
import javax.inject.Inject;

public class CreateLibraryActivity extends BaseActivity {
@Inject protected CreateLibraryPresenter presenter;

  public static Intent launch(Context context) {
    return new Intent(context, CreateLibraryActivity.class);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_library_create);

    presenter.onCreate();
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }
}
