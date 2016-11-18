package de.philipphager.disclosure.feature.app.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.util.ui.BaseActivity;
import de.philipphager.disclosure.util.ui.components.ScoreView;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.List;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class DetailActivity extends BaseActivity implements DetailView {
  private static final String EXTRA_APP = "EXTRA_APP";

  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.activity_detail) protected View view;
  @BindView(R.id.scoreView) protected ScoreView scoreView;
  @BindView(R.id.app_detail_libraries) protected RecyclerView libraryListRecyclerView;
  @Inject protected DetailPresenter presenter;
  private LibraryRecyclerAdapter adapter;

  public static Intent launch(Context context, App app) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_APP, app);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    adapter = new LibraryRecyclerAdapter();
    libraryListRecyclerView.setAdapter(adapter);
    libraryListRecyclerView.setHasFixedSize(true);
    libraryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    libraryListRecyclerView.setItemAnimator(new DefaultItemAnimator());

    adapter.setOnLibraryClickListener(library -> {
      presenter.onLibraryClicked(library);
    });

    App app = getIntent().getParcelableExtra(EXTRA_APP);
    ensureNotNull(app, "DetailActivity started without EXTRA_APP");

    presenter.onCreate(this, app);
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();

    super.onDestroy();
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void setToolbarTitle(String title) {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(title);
    }
  }

  @Override public void setAppIcon(String packageName) {
    AppIconLoader.with(this)
        .load(packageName)
        .onThread(Schedulers.io())
        .into(icon);
  }

  @Override public void setLibraries(List<Library> libraries) {
    adapter.setLibraries(libraries);
  }

  @Override public void notify(String message) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
  }

  @Override public void setScore(ScoreView.Score score) {
    scoreView.setScore(score);
  }

  @OnClick(R.id.btn_analyse) public void onAnalyseButtonClick() {
    presenter.onAnalyseButtonClick();
  }
}
