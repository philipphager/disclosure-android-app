package de.philipphager.disclosure.feature.library.category.list;

import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.feature.library.category.filter.SortByAppCount;
import de.philipphager.disclosure.feature.preference.ui.OnlyShowUsedLibraries;
import de.philipphager.disclosure.service.LibraryService;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class LibraryListPresenter {
  private final LibraryService libraryService;
  private final Preference<Boolean> onlyShowUsedLibraries;
  private CompositeSubscription subscriptions;
  private LibraryListView view;
  private Library.Type type;

  @Inject public LibraryListPresenter(LibraryService libraryService, @OnlyShowUsedLibraries
      Preference<Boolean> onlyShowUsedLibraries) {
    this.libraryService = libraryService;
    this.onlyShowUsedLibraries = onlyShowUsedLibraries;
  }

  public void onCreate(LibraryListView view, Library.Type type) {
    this.view = ensureNotNull(view, "must provide view for presenter");
    this.type = ensureNotNull(type, "must provide type of libraries to load");
    this.subscriptions = new CompositeSubscription();

    fetchFilterPreference();
    loadLibraries();
  }

  public void onDestroy() {
    subscriptions.clear();
  }

  private void fetchFilterPreference() {
    subscriptions.add(onlyShowUsedLibraries.asObservable()
        .subscribeOn(Schedulers.io())
        .subscribe(isChecked -> {
          loadLibraries();
        }, throwable -> {
          Timber.e(throwable, "while fetching onlyShowUsedLibraries preference");
        }));
  }

  private void loadLibraries() {
    subscriptions.add(libraryService.infoByType(type)
        .flatMap(libraryInfos -> Observable.from(libraryInfos)
            .filter(libraryInfo -> !onlyShowUsedLibraries.get() || libraryInfo.appCount() > 0)
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

  public void onLibraryClicked(LibraryInfo libraryInfo) {
    subscriptions.add(libraryService.byId(libraryInfo.id())
        .first()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(library -> view.navigate().toLibraryDetail(library), throwable -> {
          Timber.e(throwable, "while fetching library by id");
        }));
  }
}
