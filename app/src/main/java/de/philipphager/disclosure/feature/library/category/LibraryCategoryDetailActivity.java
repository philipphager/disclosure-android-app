package de.philipphager.disclosure.feature.library.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.feature.library.category.usecase.LibraryCategory;
import de.philipphager.disclosure.util.ui.BaseActivity;
import java.util.List;
import javax.inject.Inject;

public class LibraryCategoryDetailActivity extends BaseActivity implements LibraryCategoryDetailView {
  private static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
  @Inject protected LibraryCategoryDetailPresenter presenter;
  @BindView(R.id.libraries) protected RecyclerView libraryRecyclerView;
  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.category_title) protected TextView categoryTitle;
  private LibraryCategoryDetailRecyclerAdapter adapter;

  public static Intent launch(Context context, LibraryCategory libraryCategory) {
    Intent intent = new Intent(context, LibraryCategoryDetailActivity.class);
    intent.putExtra(EXTRA_CATEGORY, libraryCategory);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_library_category);
    getSupportActionBar().setTitle(R.string.empty);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    adapter = new LibraryCategoryDetailRecyclerAdapter(this);
    libraryRecyclerView.setAdapter(adapter);
    libraryRecyclerView.setHasFixedSize(true);
    libraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    libraryRecyclerView.setItemAnimator(new DefaultItemAnimator());
    adapter.setOnCategoryClickListener(library -> presenter.onLibraryClicked(library));

    LibraryCategory libraryCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
    presenter.onCreate(this, libraryCategory);
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void setToolbarTitle(@StringRes int title) {
    categoryTitle.setText(title);
  }

  @Override public void setLibraryCount(int count) {
    //libraryCount.setText(getResources()
    //    .getQuantityString(R.plurals.activity_library_category_library_count, count, count));
  }

  @Override public void setUsageCount(int count) {
    //libraryCount.setText(getResources()
    //    .getQuantityString(R.plurals.activity_library_category_usage_count, count, count));
  }

  @Override public void show(List<LibraryInfo> libraries) {
    adapter.setLibraries(libraries);
  }
}
