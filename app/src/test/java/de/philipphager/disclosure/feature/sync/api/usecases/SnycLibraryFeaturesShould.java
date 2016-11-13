package de.philipphager.disclosure.feature.sync.api.usecases;

import de.philipphager.disclosure.api.DisclosureApi;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import de.philipphager.disclosure.database.mocks.MockLibraryFeature;
import de.philipphager.disclosure.service.FeatureService;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class SnycLibraryFeaturesShould {
  @Mock protected DisclosureApi disclosureApi;
  @Mock protected FeatureService featureService;
  @InjectMocks protected SyncLibraryFeatures syncLibraryFeatures;

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchFeaturesFromApiAndSaveThem() {
    OffsetDateTime lastUpdated = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    when(featureService.lastUpdated()).thenReturn(Observable.just(lastUpdated));

    List<LibraryFeature> newFeatures = Collections.singletonList(MockLibraryFeature.TEST);
    when(disclosureApi.allLibraryFeatures(any())).thenReturn(Observable.just(newFeatures));

    TestSubscriber<List<LibraryFeature>> testSubscriber = new TestSubscriber<>();
    syncLibraryFeatures.run().subscribe(testSubscriber);

    verify(featureService).insertOrUpdateForLibrary(newFeatures);

    testSubscriber.assertReceivedOnNext(Collections.singletonList(newFeatures));
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void callOnErrorIfApiReturnsAnError() {
    OffsetDateTime lastUpdated = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    when(featureService.lastUpdated()).thenReturn(Observable.just(lastUpdated));

    Throwable apiError = new Throwable();
    when(disclosureApi.allLibraryFeatures(any())).thenReturn(Observable.error(apiError));

    TestSubscriber<List<LibraryFeature>> testSubscriber = new TestSubscriber<>();
    syncLibraryFeatures.run().subscribe(testSubscriber);

    testSubscriber.assertError(apiError);
  }
}
