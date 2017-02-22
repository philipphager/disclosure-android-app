package de.philipphager.disclosure.feature.library.category;

import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.feature.library.category.filter.SortByAppCount;
import de.philipphager.disclosure.feature.library.category.usecase.LibraryCategory;
import de.philipphager.disclosure.service.LibraryService;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class LibraryCategoryDetailPresenter {
  private final LibraryService libraryService;
  private LibraryCategory libraryCategory;
  private CompositeSubscription subscriptions;
  private LibraryCategoryDetailView view;

  @Inject public LibraryCategoryDetailPresenter(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  public void onCreate(LibraryCategoryDetailView view, LibraryCategory libraryCategory) {
    this.view = ensureNotNull(view, "must provide view for presenter");
    this.libraryCategory = ensureNotNull(libraryCategory, "must provide library category");
    this.subscriptions = new CompositeSubscription();

    initUi();
    loadLibraries();
  }

  public void onDestroy() {
    subscriptions.clear();
  }

  private void initUi() {
    view.setToolbarTitle(LibraryCategoryUIProvider.getTitle(libraryCategory.type()));
  }

  private void loadLibraries() {
    subscriptions.add(libraryService.infoByType(libraryCategory.type())
        .flatMap(libraryInfos -> Observable.from(libraryInfos)
            .toSortedList(new SortByAppCount()))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(libraries -> {
          view.setLibraryCount(libraries.size());
          view.show(libraries);
        }, throwable -> {
          Timber.e(throwable, "while loading libraries in category");
        }));
  }

  public void onLibraryClicked(LibraryInfo library) {
    // TODO: Navigate to library detail
  }
}
