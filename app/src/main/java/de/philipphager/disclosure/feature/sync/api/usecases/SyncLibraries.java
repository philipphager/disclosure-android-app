package de.philipphager.disclosure.feature.sync.api.usecases;

import de.philipphager.disclosure.api.DisclosureApi;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.LibraryService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class SyncLibraries {
  private final DisclosureApi disclosureApi;
  private final LibraryService libraryService;

  @Inject public SyncLibraries(DisclosureApi disclosureApi,
      LibraryService libraryService) {
    this.disclosureApi = disclosureApi;
    this.libraryService = libraryService;
  }

  public Observable<List<Library>> run() {
    return libraryService.lastUpdated()
        .first()
        .flatMap(lastUpdated -> new Paginator<Library>() {
          @Override protected Observable<List<Library>> queryPage(int page, int limit) {
            return disclosureApi.allLibraries(lastUpdated, page, limit);
          }
        }.join())
        .doOnNext(libraryService::insertOrUpdate);
  }
}
