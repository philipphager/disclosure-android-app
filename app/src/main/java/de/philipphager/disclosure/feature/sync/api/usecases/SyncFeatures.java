package de.philipphager.disclosure.feature.sync.api.usecases;

import de.philipphager.disclosure.api.DisclosureApi;
import de.philipphager.disclosure.database.feature.model.Feature;
import de.philipphager.disclosure.service.FeatureService;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class SyncFeatures {
  private final DisclosureApi disclosureApi;
  private final FeatureService featureService;

  @Inject public SyncFeatures(DisclosureApi disclosureApi,
      FeatureService featureService) {
    this.disclosureApi = disclosureApi;
    this.featureService = featureService;
  }

  public Observable<List<Feature>> run() {
    return featureService.lastUpdated()
        .first()
        .flatMap(disclosureApi::allFeatures)
        .doOnNext(featureService::insertOrUpdate);
  }
}
