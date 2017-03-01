package de.philipphager.disclosure.feature.library.category.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.util.ui.BaseFragment;
import java.util.List;
import javax.inject.Inject;

public class LibraryListFragment extends BaseFragment implements LibraryListView {
  private static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
  @Inject protected LibraryListPresenter presenter;
  @BindView(R.id.libraries) protected RecyclerView libraryRecyclerView;
  @BindView(R.id.library_count) protected TextView libraryCount;
  private LibraryListRecyclerAdapter adapter;

  public static LibraryListFragment newInstance(Library.Type type) {
    LibraryListFragment fragment = new LibraryListFragment();
    Bundle args = new Bundle();
    args.putString(EXTRA_CATEGORY, type.name());
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new LibraryListRecyclerAdapter(getContext());
    libraryRecyclerView.setAdapter(adapter);
    libraryRecyclerView.setHasFixedSize(true);
    libraryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    libraryRecyclerView.setItemAnimator(new DefaultItemAnimator());
    adapter.setOnCategoryClickListener(library -> presenter.onLibraryClicked(library));

    presenter.onCreate(this, getType());
  }

  @Override public void onDestroyView() {
    presenter.onDestroy();
    super.onDestroyView();
  }

  @Override protected int getLayout() {
    return R.layout.fragment_library_list;
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void setLibraryCount(int count) {
    libraryCount.setText(getResources()
        .getQuantityString(R.plurals.activity_library_category_library_count, count, count));
  }

  @Override public void show(List<LibraryInfo> libraries) {
    adapter.setLibraries(libraries);
  }

  private Library.Type getType() {
    return Library.Type.valueOf(getArguments().getString(EXTRA_CATEGORY));
  }
}
