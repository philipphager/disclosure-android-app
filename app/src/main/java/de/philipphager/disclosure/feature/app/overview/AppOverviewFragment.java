package de.philipphager.disclosure.feature.app.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseFragment;
import de.philipphager.disclosure.util.ui.StringProvider;
import javax.inject.Inject;

public class AppOverviewFragment extends BaseFragment {
  @BindView(R.id.appTabs) protected TabLayout appTabLayout;
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.appViewPager) protected ViewPager appViewPager;
  @Inject protected AppOverviewPresenter presenter;
  @Inject protected StringProvider stringProvider;

  public static AppOverviewFragment newInstance() {
    return new AppOverviewFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    appViewPager.setAdapter(new AppOverviewPagerAdapter(getChildFragmentManager(), stringProvider));
    appTabLayout.setupWithViewPager(appViewPager);

    presenter.onCreate();
  }

  @Override public void onDestroyView() {
    presenter.onDestroy();
    super.onDestroyView();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_app_list, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return presenter.onOptionsItemSelected(item);
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override protected int getLayout() {
    return R.layout.fragment_app_overview;
  }
}
