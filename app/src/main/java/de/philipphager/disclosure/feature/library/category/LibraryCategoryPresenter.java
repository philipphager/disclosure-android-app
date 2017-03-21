package de.philipphager.disclosure.feature.library.category;

import com.f2prateek.rx.preferences.Preference;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.analyser.library.usecase.AnalyseUsedLibraries;
import de.philipphager.disclosure.feature.preference.ui.OnlyShowUsedLibraries;
import de.philipphager.disclosure.feature.sync.api.ApiSyncer;
import de.philipphager.disclosure.service.AppService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class LibraryCategoryPresenter {
  private final Preference<Boolean> onlyShowUsedLibraries;
  private final ApiSyncer apiSyncer;
  private final AppService appService;
  private final AnalyseUsedLibraries analyseUsedLibraries;
  private LibraryCategoryView view;
  private CompositeSubscription subscriptions;

  @Inject
  public LibraryCategoryPresenter(@OnlyShowUsedLibraries Preference<Boolean> onlyShowUsedLibraries,
      ApiSyncer apiSyncer,
      AppService appService,
      AnalyseUsedLibraries analyseUsedLibraries) {
    this.onlyShowUsedLibraries = onlyShowUsedLibraries;
    this.apiSyncer = apiSyncer;
    this.appService = appService;
    this.analyseUsedLibraries = analyseUsedLibraries;
  }

  public void onCreate(LibraryCategoryView view) {
    this.view = view;
    this.subscriptions = new CompositeSubscription();
  }

  public void onDestroy() {
    this.subscriptions.clear();
  }

  public void onShowOnlyUsedLibrariesClicked() {
    onlyShowUsedLibraries.set(!onlyShowUsedLibraries.get());
  }

  public void onAddLibraryClicked() {
    view.navigate().toCreateLibrary();
  }

  public void onRefreshClicked() {
    subscriptions.add(Observable.concat(
        syncWithServerApi(),
        analyzeUsedLibrariesAndPermissions())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnCompleted(() -> Timber.d("sync completed"))
        .subscribe(result -> {
        }, throwable -> {
          Timber.e(throwable, "while syncing with device and api");
        }));
  }

  private Observable<List<Library>> syncWithServerApi() {
    return apiSyncer.sync();
  }

  private Observable<?> analyzeUsedLibrariesAndPermissions() {
    return appService.all()
        .first()
        .flatMap(Observable::from)
        .flatMap(app -> Observable.just(app)
            .subscribeOn(Schedulers.computation())
            .flatMap(analyseUsedLibraries::analyse))
        .toList();
  }
}
