package de.philipphager.disclosure.feature.library.category.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.library.category.usecase.LibraryCategory;
import de.philipphager.disclosure.util.ui.BaseFragment;
import de.philipphager.disclosure.util.ui.GridColumnProvider;
import java.util.List;
import javax.inject.Inject;

public class LibraryCategoryOverviewOverviewFragment extends BaseFragment implements
    LibraryCategoryOverviewView {
  @Inject protected LibraryCategoryOverviewPresenter presenter;
  @BindView(R.id.library_categories) RecyclerView categoryRecyclerView;
  private LibraryCategoryRecyclerAdapter adapter;

  public static LibraryCategoryOverviewOverviewFragment newInstance() {
    return new LibraryCategoryOverviewOverviewFragment();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new LibraryCategoryRecyclerAdapter(getContext());
    categoryRecyclerView.setAdapter(adapter);
    categoryRecyclerView.setHasFixedSize(true);
    categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), new GridColumnProvider(getContext()).get()));
    categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
    adapter.setOnCategoryClickListener(category -> presenter.onCategoryClicked(category));

    presenter.onCreate(this);
  }

  @Override public void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override protected int getLayout() {
    return R.layout.fragment_library_category;
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void show(List<LibraryCategory> libraryCategories) {
    adapter.setLibraryCategories(libraryCategories);
  }
}
