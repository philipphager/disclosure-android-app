package de.philipphager.disclosure.feature.library.overview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import de.philipphager.disclosure.util.ui.BaseActivity;
import javax.inject.Inject;

public class LibraryCategoryDetailActivity extends BaseActivity {
  private static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
  @Inject protected LibraryCategoryDetailPresenter presenter;

  public static Intent launch(Context context, LibraryCategory libraryCategory) {
    Intent intent = new Intent(context, LibraryCategoryDetailActivity.class);
    intent.putExtra(EXTRA_CATEGORY, libraryCategory);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_library_category);

    LibraryCategory libraryCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
    presenter.onCreate(libraryCategory);
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }
}
