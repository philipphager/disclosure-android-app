package de.philipphager.disclosure.feature.library.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
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
  @Inject protected LibraryDetailPresenter presenter;
  @BindView(R.id.overview_app_list) protected RecyclerView recyclerView;
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
    setContentView(R.layout.activity_library_overview);

    adapter = new AppPermissionRecyclerAdapter(this);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    Library library = getIntent().getParcelableExtra(EXTRA_LIBRARY);
    ensureNotNull(library, "DetailActivity started without EXTRA_LIBRARY");

    presenter.onCreate(this, library);
    adapter.setOnAppClickListener(app -> presenter.onAppClicked(app));
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override public void showApps(List<AppWithPermissions> apps) {
    adapter.setApps(apps);
  }
}
