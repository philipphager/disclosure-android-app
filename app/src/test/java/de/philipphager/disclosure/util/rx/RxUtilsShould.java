package de.philipphager.disclosure.util.rx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class RxUtilsShould {
  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void zipTheOutputsOfTwoStreamsAndSubscribeToTheResultingStream() {
    String mockItemOne = "mockItemOne";
    String mockItemTwo = "mockItemTwo";

    Observable<String> streamOne = Observable.just(mockItemOne);
    Observable<String> streamTwo = Observable.just(mockItemTwo);

    TestSubscriber<List<String>> testSubscriber = new TestSubscriber<>();

    RxUtils.zipMap(streamOne, streamTwo, (s1, s2) -> Observable.just(Arrays.asList(s1, s2)))
        .toBlocking()
        .subscribe(testSubscriber);

    testSubscriber.assertReceivedOnNext(
        Collections.singletonList(Arrays.asList(mockItemOne, mockItemTwo)));
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void failZipMapIfStreamFails() {
    String mockItemOne = "mockItemOne";

    Observable<String> streamOne = Observable.just(mockItemOne);
    Observable<String> streamTwo = Observable.error(new IllegalStateException());

    TestSubscriber<List<String>> testSubscriber = new TestSubscriber<>();

    RxUtils.zipMap(streamOne, streamTwo, (s1, s2) -> Observable.just(Arrays.asList(s1, s2)))
        .toBlocking()
        .subscribe(testSubscriber);

    testSubscriber.assertError(IllegalStateException.class);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void intRangeFromStartValue() {
    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

    RxUtils.infRange(10)
        .take(5)
        .toBlocking()
        .subscribe(testSubscriber);

    testSubscriber.assertReceivedOnNext(Arrays.asList(10, 11, 12, 13, 14));
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void notEmitEndItem() {
    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

    RxUtils.infRange(Integer.MAX_VALUE - 1)
        .take(5)
        .toBlocking()
        .subscribe(testSubscriber);

    testSubscriber.assertReceivedOnNext(Arrays.asList((Integer.MAX_VALUE - 1)));
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void emitNoValuesIfStartEqualsEnd() {
    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

    RxUtils.infRange(Integer.MAX_VALUE)
        .take(5)
        .toBlocking()
        .subscribe(testSubscriber);

    testSubscriber.assertReceivedOnNext(Collections.EMPTY_LIST);
  }
}
