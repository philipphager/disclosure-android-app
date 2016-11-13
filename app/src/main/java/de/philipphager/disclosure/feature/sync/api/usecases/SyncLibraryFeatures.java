package de.philipphager.disclosure.feature.sync.api.usecases;

import de.philipphager.disclosure.api.DisclosureApi;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import de.philipphager.disclosure.service.FeatureService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class SyncLibraryFeatures {
  private final DisclosureApi disclosureApi;
  private final FeatureService featureService;

  @Inject public SyncLibraryFeatures(DisclosureApi disclosureApi,
      FeatureService featureService) {
    this.disclosureApi = disclosureApi;
    this.featureService = featureService;
  }

  public Observable<List<LibraryFeature>> run() {
    return featureService.lastUpdatedLibraryFeatures()
        .first()
        .flatMap(disclosureApi::allLibraryFeatures)
        .doOnNext(featureService::insertOrUpdateForLibrary);
  }
}
