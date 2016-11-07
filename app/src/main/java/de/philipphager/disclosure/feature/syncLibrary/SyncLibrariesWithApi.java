package de.philipphager.disclosure.feature.syncLibrary;

import de.philipphager.disclosure.api.library.LibraryApi;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.LibraryService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class SyncLibrariesWithApi {
  private final LibraryApi libraryApi;
  private final LibraryService libraryService;

  @Inject public SyncLibrariesWithApi(LibraryApi libraryApi, LibraryService libraryService) {
    this.libraryApi = libraryApi;
    this.libraryService = libraryService;
  }

  public Observable<List<Library>> run() {
    return libraryService.lastUpdated()
        .first()
        .flatMap(libraryApi::all)
        .doOnNext(libraryService::put);
  }
}
