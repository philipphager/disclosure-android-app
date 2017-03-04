package de.philipphager.disclosure.feature.library.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppWithPermissions;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.util.ui.BaseActivity;
import java.util.List;
import javax.inject.Inject;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class LibraryDetailActivity extends BaseActivity implements LibraryDetailView {
  private static final String EXTRA_LIBRARY = "EXTRA_LIBRARY";
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.overview_app_list) protected RecyclerView recyclerView;
  @BindView(R.id.btn_open_website) protected AppCompatButton btnOpenWebsite;
  @Inject protected LibraryDetailPresenter presenter;
  private AppPermissionRecyclerAdapter adapter;

  public static Intent launch(Context context, Library library) {
    Intent intent = new Intent(context, LibraryDetailActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(EXTRA_LIBRARY, library);
    return intent;
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_library_detail);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    adapter = new AppPermissionRecyclerAdapter(this);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    Library library = getIntent().getParcelableExtra(EXTRA_LIBRARY);
    ensureNotNull(library, "AppDetailActivity started without EXTRA_LIBRARY");

    presenter.onCreate(this, library);
    adapter.setOnAppClickListener(app -> presenter.onAppClicked(app));
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override public void setToolbarTitle(String title) {
    getSupportActionBar().setTitle(title);
  }

  @Override public void setOpenWebsiteEnabled(boolean isEnabled) {
    int id = isEnabled ? R.drawable.ic_open_in_browser : R.drawable.ic_open_in_browser_disabled;
    int text = isEnabled ? R.color.color_icon : R.color.color_icon_disabled;

    Drawable openInBrowserIcon = ResourcesCompat.getDrawable(getResources(), id, null);
    btnOpenWebsite.setCompoundDrawablesWithIntrinsicBounds(null, openInBrowserIcon, null, null);
    btnOpenWebsite.setTextColor(ContextCompat.getColor(this, text));
  }

  @Override public void showApps(List<AppWithPermissions> apps) {
    adapter.setApps(apps);
  }

  @Override public void notify(@StringRes int id) {
    Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
  }

  @OnClick(R.id.btn_open_website) public void onOpenWebsiteClicked() {
    presenter.onOpenWebsiteClicked();
  }
}
