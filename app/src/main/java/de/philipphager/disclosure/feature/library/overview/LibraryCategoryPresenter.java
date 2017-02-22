package de.philipphager.disclosure.feature.library.overview;

import de.philipphager.disclosure.feature.library.overview.usecase.FetchLibraryCategories;
import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class LibraryCategoryPresenter {
  private final FetchLibraryCategories fetchLibraryCategories;
  private CompositeSubscription subscriptions;
  private LibraryCategoryView view;

  @Inject public LibraryCategoryPresenter(FetchLibraryCategories fetchLibraryCategories) {
    this.fetchLibraryCategories = fetchLibraryCategories;
  }

  public void onCreate(LibraryCategoryView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();

    subscriptions.add(fetchLibraryCategories.run()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraryCategories -> {
              view.show(libraryCategories);
            },
            throwable -> {
              Timber.e(throwable, "while fetching library categories");
            }));
  }

  public void onDestroy() {
    subscriptions.clear();
  }

  public void onCategoryClicked(LibraryCategory category) {

  }
}
