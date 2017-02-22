package de.philipphager.disclosure.feature.library.overview;

import de.philipphager.disclosure.feature.library.overview.usecase.FetchLibraryCategories;
import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class LibraryCategoryOverviewPresenter {
  private final FetchLibraryCategories fetchLibraryCategories;
  private CompositeSubscription subscriptions;
  private LibraryCategoryOverviewView view;

  @Inject public LibraryCategoryOverviewPresenter(FetchLibraryCategories fetchLibraryCategories) {
    this.fetchLibraryCategories = fetchLibraryCategories;
  }

  public void onCreate(LibraryCategoryOverviewView view) {
    this.view = ensureNotNull(view, "presenter must have a view");
    this.subscriptions = new CompositeSubscription();

    loadCategories();
  }

  public void onDestroy() {
    subscriptions.clear();
  }

  public void onCategoryClicked(LibraryCategory category) {
    if (category.allLibraries() > 0) {
      view.navigate().toCategoryDetail(category);
    }
  }

  private void loadCategories() {
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
}
