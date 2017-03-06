package de.philipphager.disclosure.feature.app.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.feature.analyser.AnalyticsProgress;
import de.philipphager.disclosure.util.ui.BaseActivity;
import de.philipphager.disclosure.util.ui.BaseFragment;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("PMD.TooManyMethods")
public class AppManagerFragment extends BaseFragment implements AppManagerView {
  @BindView(R.id.fragment_app_manager) protected LinearLayout rootView;
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.current_app_label) protected TextView currentApp;
  @BindView(R.id.pending_apps) protected TextView pendingApps;
  @BindView(R.id.analysis_status) protected View analysisStatusView;
  @BindView(R.id.progress_bar) protected ProgressBar progressBar;
  @BindView(R.id.app_recycler_view) protected RecyclerView appListRecyclerView;
  @BindView(R.id.app_count) protected TextView appCount;
  @BindView(R.id.library_count) protected TextView libraryCount;
  @Inject protected AppManagerPresenter presenter;
  private AppRecyclerAdapter adapter;
  private ActionMode actionMode;
  private final ActionMode.Callback analyzeActionModeCallback = new ActionMode.Callback() {
    @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      getActivity().getMenuInflater().inflate(R.menu.fragment_app_list_analyze, menu);
      return true;
    }

    @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false;
    }

    @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      if (item.getItemId() == R.id.action_analyze) {
        presenter.onAnalyzeSelectedAppsClicked();
        mode.finish();
        return true;
      }
      return false;
    }

    @Override public void onDestroyActionMode(ActionMode mode) {
      actionMode = null;
      presenter.onEndActionMode();
    }
  };

  public static AppManagerFragment newInstance() {
    return new AppManagerFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    adapter = new AppRecyclerAdapter(getContext());
    appListRecyclerView.setAdapter(adapter);
    appListRecyclerView.setHasFixedSize(true);
    appListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    appListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    adapter.setOnAppClickListener(presenter::onAppClicked);
    adapter.setOnAppLongClickListener(presenter::onAppLongClicked);

    presenter.onViewCreated(this);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_app_list, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return presenter.onOptionsItemSelected(item);
  }

  @Override public void onDestroyView() {
    presenter.onDestroyView();
    super.onDestroyView();
  }

  @Override protected int getLayout() {
    return R.layout.fragment_app_manager;
  }

  @Override protected void injectFragment(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void show(List<AppReport> appReports) {
    adapter.setAppReports(appReports);
  }

  @Override public void showAppCount(int count) {
    appCount.setText(getResources()
        .getQuantityString(R.plurals.fragment_app_manager_app_count, count, count));
  }

  @Override public void showLibraryCount(int count) {
    libraryCount.setText(getResources()
        .getQuantityString(R.plurals.app_list_item_libraries_found, count, count));
  }

  @Override public void startActionMode() {
    actionMode = ((BaseActivity) getActivity()).startSupportActionMode(analyzeActionModeCallback);
  }

  @Override public void showActionModeTitle(String title) {
    if (hasActionMode()) {
      actionMode.setTitle(title);
    }
  }

  @Override public boolean hasActionMode() {
    return actionMode != null;
  }

  @Override public void showAnalysisProgress() {
    if (analysisStatusView.getVisibility() == View.GONE) {
      TransitionManager.beginDelayedTransition(rootView);
      progressBar.setVisibility(View.VISIBLE);
      analysisStatusView.setVisibility(View.VISIBLE);
    }
  }

  @Override public void setAnalysisProgress(AnalyticsProgress.State state) {
    int progress = 0;

    if (state == AnalyticsProgress.State.COMPLETE || state == AnalyticsProgress.State.CANCEL) {
      hideAnalysisProgress();
    } else {
      showAnalysisProgress();
    }

    switch (state) {
      case START:
        resetProgress();
        break;
      case EXTRACT_APK:
        progress = 15;
        break;
      case FILTER_METHODS:
        progress = 45;
        break;
      case ANALYSE_METHODS:
        progress = 75;
        break;
      case COMPLETE:
        setAnalysisCompleted();
        break;
      default:
        Timber.d("Unhandled state %s", state);
        break;
    }

    progressBar.setProgress(progress);
  }

  @Override public void setAnalysisCompleted() {
    progressBar.setProgress(100);
  }

  @Override public void hideAnalysisProgress() {
    if (analysisStatusView.getVisibility() == View.VISIBLE) {
      TransitionManager.beginDelayedTransition(rootView);
      analysisStatusView.setVisibility(View.GONE);
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override public void resetProgress() {
    progressBar.setProgress(0);
  }

  @Override public void showCancel() {
    // TODO
  }

  @Override public void showPendingApp(App app) {
    // TODO
  }

  @Override public void notify(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
  }

  @Override public void showCurrentAnalysedApp(String appLabel) {
    currentApp.setText(appLabel);
  }

  @Override public void showCancelPendingApps(int count) {
    pendingApps.setText(getString(R.string.fragment_app_manager_pending_apps, count));
    pendingApps.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
  }

  @OnClick(R.id.pending_apps) public void onCancelPendingAppsClicked() {
    presenter.onCancelPendingAppsClicked();
  }
}
