package de.philipphager.disclosure.feature.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.util.ui.BaseFragment;
import java.util.List;
import javax.inject.Inject;

public class AppOverviewFragment extends BaseFragment implements AppOverviewView {
  @Inject protected AppOverviewPresenter presenter;
  @BindView(R.id.overview_app_list) protected RecyclerView appListRecyclerView;
  @BindView(R.id.progressBar) protected ProgressBar progressBar;
  private AppRecyclerAdapter adapter;

  public static AppOverviewFragment newInstance() {
    return new AppOverviewFragment();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new AppRecyclerAdapter(getContext());
    appListRecyclerView.setAdapter(adapter);
    appListRecyclerView.setHasFixedSize(true);
    appListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    appListRecyclerView.setItemAnimator(new DefaultItemAnimator());

    adapter.setOnAppClickListener(app -> {
      presenter.onAppClicked(app);
    });

    presenter.onCreate(this);
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override protected int getLayout() {
    return R.layout.fragment_overview;
  }

  @Override public void notify(String message) {
    Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
  }

  @Override public void show(List<App> apps) {
    adapter.setApps(apps);
  }

  @Override public void showProgress() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }
}
