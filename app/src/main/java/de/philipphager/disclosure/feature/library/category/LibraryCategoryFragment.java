package de.philipphager.disclosure.feature.library.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseFragment;
import de.philipphager.disclosure.util.ui.StringProvider;
import javax.inject.Inject;

public class LibraryCategoryFragment extends BaseFragment {
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.category_view_pager) protected ViewPager categoryViewPager;
  @BindView(R.id.category_tab_layout) protected TabLayout categoryTabLayout;
  @Inject protected StringProvider stringProvider;
  @Inject protected LibraryCategoryPresenter presenter;

  public static LibraryCategoryFragment newInstance() {
    return new LibraryCategoryFragment();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    categoryViewPager.setAdapter(new LibraryCategoryPagerAdapter(getChildFragmentManager(), stringProvider));
    categoryTabLayout.setupWithViewPager(categoryViewPager);
  }

  @Override protected int getLayout() {
    return R.layout.fragment_library_category;
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @OnClick(R.id.ic_filter) void onShowOnlyUsedLibrariesClicked() {
    presenter.onShowOnlyUsedLibrariesClicked();
  }
}
