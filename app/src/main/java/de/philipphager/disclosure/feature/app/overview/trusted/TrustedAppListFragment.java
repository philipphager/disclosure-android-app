package de.philipphager.disclosure.feature.app.overview.trusted;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.feature.app.overview.list.AppListView;
import de.philipphager.disclosure.feature.app.overview.list.AppRecyclerAdapter;
import de.philipphager.disclosure.util.ui.BaseFragment;
import java.util.List;
import javax.inject.Inject;

public class TrustedAppListFragment extends BaseFragment implements AppListView {
  @BindView(R.id.app_recycler_view) protected RecyclerView appListRecyclerView;
  @BindView(R.id.app_count) protected TextView appCount;
  @BindView(R.id.progress_bar) protected ProgressBar progressBar;
  @BindView(R.id.library_count) protected TextView libraryCount;
  @Inject protected TrustedAppListPresenter presenter;
  private AppRecyclerAdapter adapter;

  public static TrustedAppListFragment newInstance() {
    return new TrustedAppListFragment();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new AppRecyclerAdapter(getContext());
    appListRecyclerView.setAdapter(adapter);
    appListRecyclerView.setHasFixedSize(true);
    appListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    appListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    adapter.setOnAppClickListener(app -> presenter.onAppClicked(app));

    presenter.onCreate(this);
  }

  @Override public void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override protected int getLayout() {
    return R.layout.fragment_app_list;
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void show(List<AppWithLibraries> apps) {
    adapter.setApps(apps);
  }

  @Override public void showAppCount(int count) {
    appCount.setText(getResources()
        .getQuantityString(R.plurals.fragment_app_list_app_count, count, count));
  }

  @Override public void showLibraryCount(int count) {
    libraryCount.setText(getResources()
        .getQuantityString(R.plurals.fragment_app_list_library_count, count, count));
  }

  @Override public void showProgress() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    progressBar.setVisibility(View.INVISIBLE);
  }
}
