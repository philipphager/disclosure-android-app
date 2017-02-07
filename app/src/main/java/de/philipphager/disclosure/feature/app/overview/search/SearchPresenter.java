package de.philipphager.disclosure.feature.app.overview.search;

import de.philipphager.disclosure.service.app.AppService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class SearchPresenter {
  private final AppService appService;
  private AppSearchView view;
  private CompositeSubscription subscriptions;

  @Inject public SearchPresenter(AppService appService) {
    this.appService = appService;
  }

  public void onCreate(AppSearchView view) {
    this.view = view;

    subscriptions = new CompositeSubscription();
    fetchSearchQueryUpdates();
  }

  public void onDestroy() {
    subscriptions.unsubscribe();
  }

  private void fetchSearchQueryUpdates() {
    subscriptions.add(view.getSearchQuery()
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribe(this::onSearchQueryChanged, throwable -> {
          Timber.e(throwable, "while observing selectByQuery query changes");
        }));
  }

  private void onSearchQueryChanged(String query) {
    subscriptions.add(appService.search(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(apps -> {
          if (query.isEmpty()) {
            view.hideEmptySearchView();
          } else if (apps.isEmpty()) {
            view.showEmptySearchView();
          } else {
            view.showApps(apps);
          }
        }, throwable -> {
          Timber.e(throwable, "while searching for apps");
        })
    );
  }

  public void onActivityDismissedClicked() {
    view.finish();
  }
}
