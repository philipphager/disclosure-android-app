package de.philipphager.disclosure.feature.sync.api.usecases;

import de.philipphager.disclosure.api.DisclosureApi;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.mocks.MockLibrary;
import de.philipphager.disclosure.service.LibraryService;
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

@RunWith(MockitoJUnitRunner.class) public class SyncLibrariesShould {
  @Mock protected DisclosureApi disclosureApi;
  @Mock protected LibraryService libraryService;
  @InjectMocks protected SyncLibraries syncLibraries;

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void fetchFeaturesFromApiAndSaveThem() {
    OffsetDateTime lastUpdated = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    when(libraryService.lastUpdated()).thenReturn(Observable.just(lastUpdated));

    List<Library> newLibraries = Collections.singletonList(MockLibrary.TEST);
    when(disclosureApi.allLibraries(any())).thenReturn(Observable.just(newLibraries));

    TestSubscriber<List<Library>> testSubscriber = new TestSubscriber<>();
    syncLibraries.run().subscribe(testSubscriber);

    verify(disclosureApi).allLibraries(lastUpdated);
    verify(libraryService).insertOrUpdate(newLibraries);

    testSubscriber.assertReceivedOnNext(Collections.singletonList(newLibraries));
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void callOnErrorIfApiReturnsAnError() {
    OffsetDateTime lastUpdated = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    when(libraryService.lastUpdated()).thenReturn(Observable.just(lastUpdated));

    Throwable apiError = new Throwable();
    when(disclosureApi.allLibraries(any())).thenReturn(Observable.error(apiError));

    TestSubscriber<List<Library>> testSubscriber = new TestSubscriber<>();
    syncLibraries.run().subscribe(testSubscriber);

    testSubscriber.assertError(apiError);
  }
}
