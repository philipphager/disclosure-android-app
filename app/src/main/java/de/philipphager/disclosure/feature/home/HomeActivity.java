package de.philipphager.disclosure.feature.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.util.ui.BaseActivity;
import javax.inject.Inject;

public class HomeActivity extends BaseActivity {
  private static final boolean SMOOTH_SCROLL_ENABLED = true;

  @BindView(R.id.view_pager) protected ViewPager viewPager;
  @BindView(R.id.bottom_navigation) protected AHBottomNavigation bottomNavigation;
  @Inject protected HomePresenter presenter;

  public static Intent launch(Context context) {
    return new Intent(context, HomeActivity.class);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    setupViewPager();
    setupBottomNavigation();

    presenter.onCreate();
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  private void setupViewPager() {
    HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager());
    viewPager.setAdapter(homeAdapter);
  }

  private void setupBottomNavigation() {
    AHBottomNavigationAdapter adapter = new AHBottomNavigationAdapter(this, R.menu.navigation_home);
    adapter.setupWithBottomNavigation(bottomNavigation, null);

    bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
      viewPager.setCurrentItem(position, SMOOTH_SCROLL_ENABLED);
      return true;
    });
  }
}
