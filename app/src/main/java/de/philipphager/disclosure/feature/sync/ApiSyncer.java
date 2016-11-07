package de.philipphager.disclosure.feature.sync;

import de.philipphager.disclosure.api.library.LibraryApi;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.LibraryService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class ApiSyncer {
  private final LibraryApi libraryApi;
  private final LibraryService libraryService;

  @Inject public ApiSyncer(LibraryApi libraryApi, LibraryService libraryService) {
    this.libraryApi = libraryApi;
    this.libraryService = libraryService;
  }

  public Observable<List<Library>> sync() {
    return libraryService.lastUpdated()
        .first()
        .flatMap(libraryApi::all)
        .doOnNext(libraryService::put);
  }
}
