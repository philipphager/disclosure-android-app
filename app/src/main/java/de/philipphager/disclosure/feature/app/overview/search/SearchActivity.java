package de.philipphager.disclosure.feature.app.overview.search;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.feature.app.overview.list.AppRecyclerAdapter;
import de.philipphager.disclosure.util.ui.BaseActivity;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;

public class SearchActivity extends BaseActivity implements AppSearchView {
  private final PublishSubject<String> searchQuerySubject = PublishSubject.create();
  private final ObjectAnimator[] objectAnimators = new ObjectAnimator[2];
  @Inject protected SearchPresenter presenter;
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.dismiss_activity) protected View dismissView;
  @BindView(R.id.search_view) protected SearchView searchView;
  @BindView(R.id.search_results) protected RecyclerView searchResult;
  @BindView(R.id.empty_search_results) protected View emptySearchView;
  @BindInt(android.R.integer.config_shortAnimTime) int shortAnimTime;
  private AppRecyclerAdapter adapter;
  private String query;

  public static Intent launch(Context context) {
    return new Intent(context, SearchActivity.class);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setupSearchView();

    adapter = new AppRecyclerAdapter(this);
    searchResult.setAdapter(adapter);
    searchResult.setHasFixedSize(true);
    searchResult.setLayoutManager(new LinearLayoutManager(this));
    searchResult.setItemAnimator(new DefaultItemAnimator());

    presenter.onCreate(this);
    overridePendingTransition(0, R.anim.abc_popup_enter);
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  private void setupSearchView() {
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.onActionViewExpanded();
    // Hint, inputType & ime options seem to be ignored from XML! Set in code
    searchView.setQueryHint(getString(R.string.app_search_hint));
    searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH
        | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String query) {
        animateBackground(query.isEmpty());
        searchQuerySubject.onNext(query);
        return true;
      }
    });
  }

  private void handleIntent(Intent intent) {
    if (intent.hasExtra(SearchManager.QUERY)) {
      query = intent.getStringExtra(SearchManager.QUERY);
      searchView.setQuery(query, true);
      searchQuerySubject.onNext(query);
    }
  }

  private void animateBackground(boolean emptyQuery) {
    int toAlpha = emptyQuery ? 0 : 1;
    objectAnimators[0] = ObjectAnimator.ofFloat(emptySearchView, "alpha", toAlpha);
    objectAnimators[1] = ObjectAnimator.ofFloat(searchResult, "alpha", toAlpha);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(objectAnimators);
    animatorSet.setDuration(shortAnimTime);
    animatorSet.start();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  @Override public void showApps(List<AppWithLibraries> apps) {
    dismissView.setVisibility(View.GONE);
    emptySearchView.setVisibility(View.GONE);
    adapter.setApps(apps);
  }

  @Override public void showEmptySearchView() {
    dismissView.setVisibility(View.GONE);
    emptySearchView.setVisibility(View.VISIBLE);
    adapter.setApps(Collections.emptyList());
  }

  @Override public void hideEmptySearchView() {
    dismissView.setVisibility(View.VISIBLE);
    emptySearchView.setVisibility(View.GONE);
  }

  @Override public Observable<String> getSearchQuery() {
    return searchQuerySubject;
  }

  @OnClick(R.id.dismiss_activity) public void onActivityDismissedClicked() {
    presenter.onActivityDismissedClicked();
  }
}
