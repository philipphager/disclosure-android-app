package de.philipphager.disclosure.feature.sync.api;

import de.philipphager.disclosure.feature.sync.api.usecases.SyncFeatures;
import de.philipphager.disclosure.feature.sync.api.usecases.SyncLibraries;
import de.philipphager.disclosure.feature.sync.api.usecases.SyncLibraryFeatures;
import de.philipphager.disclosure.util.time.Stopwatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class) public class ApiSyncerShould {
  @Mock protected SyncLibraries syncLibraries;
  @Mock protected SyncFeatures syncFeatures;
  @Mock protected SyncLibraryFeatures syncLibraryFeatures;
  @Mock protected Stopwatch stopwatch;
  @InjectMocks protected ApiSyncer apiSyncer;

  @Test public void syncLibraiesAndFeaturesBeforeLibraryFeatures() {
    TestSubscriber testSubscriber = new TestSubscriber();

    apiSyncer.sync().subscribe(testSubscriber);

    InOrder inOrder = inOrder(syncLibraries, syncFeatures, syncLibraryFeatures);

    inOrder.verify(syncLibraries).run();
    inOrder.verify(syncFeatures).run();
    inOrder.verify(syncLibraryFeatures).run();
  }
}
